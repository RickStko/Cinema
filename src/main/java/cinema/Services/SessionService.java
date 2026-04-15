package cinema.Services;

import cinema.Models.Session;
import cinema.Repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public Session createSession(Session session){
        if(session.getAge_restrictions() < 0 || session.getAge_restrictions() > 21){
            throw new IllegalArgumentException("Некоректне вікове обмеження!");
        }
        return sessionRepository.save(session);
    }

    public List<Session> getAllSessions(){
        return sessionRepository.findAll();
    }
    public Session getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сеанс з ID " + id + " не знайдено"));
    }

    public Session updateSession(Long id, Session updatedSession){
        Session session = sessionRepository.findById(id).orElseThrow();
        session.setName(updatedSession.getName());
        session.setGenres(updatedSession.getGenres());
        session.setDuration(updatedSession.getDuration());
        session.setDirector(updatedSession.getDirector());
        session.setPlot(updatedSession.getPlot());
        session.setAge_restrictions(updatedSession.getAge_restrictions());
        return sessionRepository.save(session);
    }

    public void deleteSession(Long id){
        sessionRepository.deleteById(id);
    }
}
