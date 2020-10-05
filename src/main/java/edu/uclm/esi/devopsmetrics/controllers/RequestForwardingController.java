package edu.uclm.esi.devopsmetrics.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
/**
 * @author FcoCrespo
 */
public class RequestForwardingController {
  @RequestMapping(value = "/**/{[path:[^\\.]*}")
  /**
   * @author FcoCrespo
   */
  public String redirect() {
    // Forward to home page so that angular routing is preserved.
    return "forward:/";
  }
}