package cinema.Services;

import cinema.Models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TmdbService {

    @Autowired
    private RestTemplate restTemplate;

    private final String API_KEY = "f081d5b850af5a9716030216e2693cb9";

    public Session fetchMovieDetails(String movieId) {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + API_KEY + "&language=uk-UA&append_to_response=credits";

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Session session = new Session();

        if (response == null) return session;

        session.setName((String) response.get("title"));
        session.setPlot((String) response.get("overview"));

        if (response.get("runtime") != null) {
            session.setDuration(((Number) response.get("runtime")).doubleValue());
        }

        List<Map<String, Object>> genresList = (List<Map<String, Object>>) response.get("genres");
        if (genresList != null) {
            String genresStr = genresList.stream()
                    .map(g -> (String) g.get("name"))
                    .collect(Collectors.joining(", "));
            session.setGenres(genresStr);
        }

        Boolean isAdult = (Boolean) response.get("adult");
        session.setAge_restrictions((isAdult != null && isAdult) ? 18 : 12);

        Map<String, Object> credits = (Map<String, Object>) response.get("credits");
        if (credits != null) {
            List<Map<String, Object>> crew = (List<Map<String, Object>>) credits.get("crew");
            if (crew != null) {
                for (Map<String, Object> member : crew) {
                    if ("Director".equals(member.get("job"))) {
                        session.setDirector((String) member.get("name"));
                        break;
                    }
                }
            }
        }

        return session;
    }
}