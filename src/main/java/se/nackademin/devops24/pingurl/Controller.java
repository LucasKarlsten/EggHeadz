package se.nackademin.devops24.pingurl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import se.nackademin.devops24.pingurl.model.PingedURL;

import java.util.Collection;

@org.springframework.stereotype.Controller
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final PingUrlService pingUrlService;

    public Controller(PingUrlService pingUrlService) {
        this.pingUrlService = pingUrlService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        var modelAndView = new ModelAndView("index");
        logger.info("Handling request to /");

        Collection<PingedURL> urls = pingUrlService.getPingUrls();

        modelAndView.addObject("urls", urls);
        return modelAndView;
    }

    @PostMapping("/pingurl")
    public RedirectView addUrl(@RequestParam String url, @RequestParam String name) {
        logger.info("Received new URL: {} with name: {}", url, name);
        pingUrlService.addPingUrl(url, name);
        return new RedirectView("/");
    }

    // 🔹 ta bort skapad ping-url
    @PostMapping("/pingurl/delete")
    public RedirectView deleteUrl(@RequestParam String name) {
        logger.info("Deleting URL with name: {}", name);
        pingUrlService.deleteUrlFromPing(name);
        return new RedirectView("/");
    }

    // 🔹 manuellt "Ping nu"
    @PostMapping("/pingurl/ping")
    public RedirectView pingNow(@RequestParam String name) {
        logger.info("Manually pinging URL with name: {}", name);
        pingUrlService.pingUrlNow(name);
        return new RedirectView("/");
    }
}