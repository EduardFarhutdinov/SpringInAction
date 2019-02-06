package tacos.web.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tacos.Taco;
import tacos.data.TacoRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/design",
        produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoController {
    private TacoRepository tacoRepo;

    @Autowired
    EntityLinks entityLinks;

    public DesignTacoController(TacoRepository tacoRepo) {
        this.tacoRepo = tacoRepo;
    }
//
//        @GetMapping("/recent")
//    public Iterable<Taco> recentTacos() {
//        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());
//
//        return tacoRepo.findAll(pageRequest).getContent();
//    }
//    @GetMapping("/recent")
//    public Resources<Resource<Taco>> recentTacos() {
//        PageRequest page = PageRequest.of(
//                0, 12, Sort.by("createdAt").descending());
//
//        List<Taco> tacos = tacoRepo.findAll(page).getContent();
//        Resources<Resource<Taco>> recentResources = Resources.wrap(tacos);
//
//        recentResources.add(ControllerLinkBuilder.linkTo(DesignTacoController.class).slash("recent").withRel("resents"));
//        return recentResources;
//    }


    @GetMapping("/recent")
    public Resources<TacoResource> recentTacos() { PageRequest page = PageRequest.of(
            0, 12, Sort.by("createdAt").descending()); List<Taco> tacos = tacoRepo.findAll(page).getContent();
        List<TacoResource> tacoResources =
                new TacoResourceAssembler().toResources(tacos); Resources<TacoResource> recentResources =
                new Resources<TacoResource>(tacoResources);
                recentResources.add(
                linkTo(methodOn(DesignTacoController.class).recentTacos())
                        .withRel("recents"));
                return recentResources;
    }


    @GetMapping("/{id}")
    public Taco tacoById(@PathVariable("id") Long id) {
        Optional<Taco> optionalTaco = tacoRepo.findById(id);
        if (optionalTaco.isPresent()) {
            return optionalTaco.get();
        }
        return null;
    }
//    @GetMapping("/{id}")
//    public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) { Optional<Taco> optTaco = tacoRepo.findById(id);
//        if (optTaco.isPresent()) {
//            return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>((Taco) null, HttpStatus.NOT_FOUND);
//    }


    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody Taco taco) {
        return tacoRepo.save(taco);
    }

}
