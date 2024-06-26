package mate.academy.dao;

import java.util.Optional;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Movie;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Dao
public class MovieDaoImpl implements MovieDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Movie add(Movie movie) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(movie);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new DataProcessingException("Can't add movie to database", e);
            }
        } catch (Exception e) {
            throw new DataProcessingException("Unable to open session", e);
        }
        return movie;
    }

    @Override
    public Optional<Movie> get(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Movie.class, id));
        } catch (Exception e) {
            throw new DataProcessingException("Unable to open session", e);
        }
    }
}
