package cinema.Controller;

import cinema.Models.Session;
import cinema.Services.SessionService;
import cinema.Services.TmdbService;
import cinema.Repositories.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private TmdbService tmdbService;

    @Autowired
    private CinemaRepository cinemaRepository;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("allSessions", sessionService.getAllSessions());
        return "sessions-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("session", new Session());
        model.addAttribute("allCinemas", cinemaRepository.findAll());
        return "session-form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("session") Session session,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allCinemas", cinemaRepository.findAll());
            return "session-form";
        }
        sessionService.createSession(session);
        return "redirect:/sessions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Session session = sessionService.getSessionById(id);

        model.addAttribute("session", session);
        model.addAttribute("allCinemas", cinemaRepository.findAll());
        return "session-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteSession(@PathVariable("id") Long id) {
        sessionService.deleteSession(id);
        return "redirect:/sessions";
    }

    @GetMapping("/import")
    public String importFromTmdb(@RequestParam("tmdbId") String tmdbId, Model model) {
        try {
            Session importedSession = tmdbService.fetchMovieDetails(tmdbId);
            model.addAttribute("session", importedSession);
            model.addAttribute("allCinemas", cinemaRepository.findAll());
            return "session-form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Фільм з таким ID не знайдено в TMDB");
            model.addAttribute("session", new Session());
            model.addAttribute("allCinemas", cinemaRepository.findAll());
            return "session-form";
        }
    }
}