package pt.ipb.dsys.assessment.four.app3;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import pt.ipb.dsys.assessment.four.common.UpdateService;

/**
 * 4th Assessment (6)
 * Ring Divide (variant-2)
 * Fernando Furtado (314866)
 */
@SpringBootApplication(scanBasePackageClasses = {App3Main.class, UpdateService.class})
public class App3Main {

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(App3Main.class)
        .bannerMode(Banner.Mode.OFF)
        .run(args);
  }

}
