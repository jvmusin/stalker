//package musin.seeker.web;
//
//import lombok.RequiredArgsConstructor;
//import musin.seeker.db.RelationChangeService;
//import musin.seeker.db.seeker.SeekerService;
//import musin.seeker.db.model.RelationChange;
//import musin.seeker.db.seeker.Seeker;
//import musin.seeker.vk.api.SimpleVkUser;
//import musin.seeker.vk.api.VkApi;
//import musin.seeker.vk.relation.VkRelationList;
//import musin.seeker.vk.relation.VkRelationListPuller;
//import musin.seeker.vk.relation.VkUpdateImpl;
//import musin.seeker.web.dto.*;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.util.List;
//
//import static java.util.Comparator.comparingInt;
//import static java.util.stream.Collectors.toList;
//import static org.springframework.web.bind.annotation.RequestMethod.POST;
//
//@Controller
//@RequiredArgsConstructor
//public class MainController {
//
//  private final SeekerService seekerService;
//  private final RelationChangeService relationChangeService;
//  private final VkApi vkApi;
//  private final VkRelationListPuller vkRelationListPuller;
//
//  @RequestMapping("/")
//  public String index() {
//    return "redirect:/seekers";
//  }
//
//  @RequestMapping("/seekers")
//  public String seekers(Model model) {
//    List<SeekerDto> seekers = seekerService.findAll().stream()
//        .map(Seeker::getOwner)
//        .map(this::createSeekerDto)
//        .collect(toList());
//    model.addAttribute("model", new SeekerListDto(seekers));
//    model.addAttribute("newSeeker", new NewSeekerDto());
//    return "seekers";
//  }
//
//  @RequestMapping("/seekers/{owner}/changes")
//  public ModelAndView changes(@PathVariable("owner") int owner) {
//    List<RelationChange> changes = relationChangeService.findAllByOwner(owner).join().stream()
//        .filter(rc -> !rc.isHidden())
//        .collect(toList());
//    List<Integer> userIds = changes.stream()
//        .map(RelationChange::getTarget)
//        .collect(toList());
//    vkApi.loadUsers(userIds);
//    List<RelationChangeDto> changeDtos = changes.stream()
//        .map(this::createRelationChangeDto)
//        .sorted(comparingInt(RelationChangeDto::getId).reversed())
//        .collect(toList());
//    return new ModelAndView("changes", "model", new FullSeekerDto(
//        createSeekerDto(owner),
//        changeDtos
//    ));
//  }
//
//  @RequestMapping("/seekers/all_changes")
//  public String allChanges(Model model) {
//    List<RelationChange> changes = relationChangeService.findAll().stream()
//        .filter(rc -> !rc.isHidden())
//        .collect(toList());
//    List<Integer> userIds = changes.stream()
//        .map(RelationChange::getTarget)
//        .collect(toList());
//    vkApi.loadUsers(userIds);
//    List<RelationChangeDto> changeDtos = changes.stream()
//        .map(this::createRelationChangeDto)
//        .sorted(comparingInt(RelationChangeDto::getId).reversed())
//        .collect(toList());
//    model.addAttribute("model", new FullSeekerDto(
//        new SeekerDto(-1, "All"),
//        changeDtos
//    ));
//    return "changes";
//  }
//
//  @RequestMapping(value = "/seekers", method = POST)
//  public String registerSeeker(@ModelAttribute NewSeekerDto newSeeker) {
//    VkRelationList list = vkRelationListPuller.pull(newSeeker.userId).join();
//    List<RelationChange> changes = list.stream()
//        .map(relation -> new VkUpdateImpl(relation.getUser(), null, relation))
//        .map(update -> update.toRelationChange(newSeeker.userId))
//        .collect(toList());
//    seekerService.create(newSeeker.getUserId(), changes);
//    return "redirect:/seekers";
//  }
//
//  private RelationChangeDto createRelationChangeDto(RelationChange rc) {
//    return new RelationChangeDto(
//        rc.getId(),
//        rc.getTime(),
//        createSeekerDto(rc.getOwner()),
//        createSeekerDto(rc.getTarget()),
//        rc.getPrevType(),
//        rc.getCurType()
//    );
//  }
//
//  private SeekerDto createSeekerDto(int owner) {
//    SimpleVkUser user = vkApi.loadUser(owner);
//    String name = String.format("%s %s", user.getFirstName(), user.getLastName());
//    return new SeekerDto(owner, name);
//  }
//}