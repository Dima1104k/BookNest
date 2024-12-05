package com.example.demo.controlles;
import com.example.demo.models.Publisher;
import com.example.demo.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping("/publisher/new")
    public String createPublishers(Model model) {
        model.addAttribute("publishers", new Publisher());
        return "publishers-form";
    }

    @PostMapping("/publisher/new")
    public String createPublisher(@RequestParam String name, @RequestParam String address, @RequestParam String phone,  @RequestParam String website,Model model) {
       Publisher publisher  = new Publisher ();
        publisher.setName(name);
        publisher.setAddress(address);
        publisher.setPhone(phone);
        publisher.setWebsite(website);
        if(!publisherService.savePublisher(publisher)) {
            model.addAttribute("error", "Видавництво: " + publisher.getName() + " уже існує");
            return "publishers-form";
        }
        publisherService.savePublisher(publisher);
        return "redirect:/publishers/list";
    }

    @GetMapping("/publishers/list")
    public String listPublisher(Model model) {
        model.addAttribute("publishers", publisherService.getAllPublishers());
        return "publishers-list";
    }

    @PostMapping("/publisher/delete/{id}")
    public String delete(@PathVariable Long id){
        publisherService.deletePublisher(id);
        return "redirect:/publishers/list";
    }

    @GetMapping("/publisher/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
       Publisher publishers = publisherService.getPublisherById(id);
        model.addAttribute("publishers",publishers);
        return "publisher-edit";
    }



    @PostMapping("/publisher/edit/{id}")
    public String updatePublisher(@PathVariable Long id,@RequestParam String name, @RequestParam String address, @RequestParam String phone,  @RequestParam String website) {
        Publisher updatePublisher= publisherService.getPublisherById(id);
        updatePublisher.setName(name);
        updatePublisher.setWebsite(website);
        updatePublisher.setPhone(phone);
        updatePublisher.setAddress(address);
        publisherService.updatedPublisher(id,updatePublisher);
        return "redirect:/publishers/list";
    }

}
