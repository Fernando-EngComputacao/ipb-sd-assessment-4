package pt.ipb.dsys.assessment.four.app1;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import pt.ipb.dsys.assessment.four.common.UpdateService;

/**
 * 4th Assessment (6)
 * Ring Divide (variant-2)
 * Fernando Furtado (314866)
 */
@SpringBootApplication(scanBasePackageClasses = {App1Main.class, UpdateService.class})
public class App1Main {

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(App1Main.class)
        .bannerMode(Banner.Mode.OFF)
        .run(args);
  }

}
