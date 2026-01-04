package com.sprint.mission.discodeit.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

  @GetMapping("/login")
  public String login() {
    return "index";
  }
}
