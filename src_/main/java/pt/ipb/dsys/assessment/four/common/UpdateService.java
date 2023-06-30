package pt.ipb.dsys.assessment.four.common;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pt.ipb.dsys.assessment.four.model.ShouldUpdateView;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class UpdateService {

  private static final Logger logger = LoggerFactory.getLogger(UpdateService.class);

  public static final String BASE_URI = "http://127.0.0.1:8989/api";

  public final RestTemplate restTemplate;

  private final ApplicationContext applicationContext;

  public UpdateService(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.restTemplate = new RestTemplate();
  }

  public boolean shouldUpdate() {
    try {
      String uriString = String.format("%s/resource/should-update", BASE_URI);
      ShouldUpdateView shouldUpdate = restTemplate
          .getForObject(
              UriComponentsBuilder.fromUriString(uriString)
                  .queryParam("clientName", extractAppName())
                  .encode()
                  .toUriString(),
              ShouldUpdateView.class
          );
      assert shouldUpdate != null;
      return shouldUpdate.isShouldUpdate();
    } catch (ResourceAccessException e) {
      logger.error("Error accessing api:\n- {}\nIs the docker container running?", e.getMessage());
      System.exit(0);
      return false;
    }
  }

  private final static Pattern APP_MAIN_TEMPLATE = Pattern.compile("pt.*(App[0-9]+Main).*");

  private String extractAppName() {
    Map<String, Object> annotatedBeans = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);
    Preconditions.checkArgument(!annotatedBeans.isEmpty(), "Couldn't find @SpringBootApplication annotated classes");
    return annotatedBeans.values()
        .stream()
        .map(cl -> APP_MAIN_TEMPLATE.matcher(cl.getClass().getName()))
        .map(m -> m.matches() ? m.group(1) : null)
        .filter(Objects::nonNull)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Couldn't find class App[0-9]Main, did you rename it?"));
  }

}
