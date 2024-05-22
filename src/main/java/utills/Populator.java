package utills;

import config.HibernateConfig;
import jakarta.persistence.EntityManager;
import restSecrurity.DOA.memoryDAO.DudeMemoryDAO;

import restSecrurity.persistance.*;
import restSecrurity.persistance.Thread;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Populator {

    public static void populateDatabase(boolean isTest)  {
            EntityManager em = HibernateConfig.getEntityManagerFactoryConfig(isTest).createEntityManager();
            em.getTransaction().begin();

            try {
                Role user = new Role();
                Role admin = new Role();
                user.setName("user");
                admin.setName("admin");
                em.persist(user);
                em.persist(admin);

                // Initialize some users
                User user1 = new User();
                user1.setUsername("patrick@user1.com");
                user1.setPassword("password123");
                user1.setEmail("patrick@user1.com");
                Set<Role> roles = new HashSet<>();
                roles.add(new Role("user"));
                user1.setRoles(roles);
                em.persist(user1);

                // Initialize some categories
                Category category1 = new Category();
                category1.setName("General Discussion");
                Category category2 = new Category();
                category2.setName("Technical Support");

                em.persist(category1);
                em.persist(category2);

                // Create Threads
                Thread thread1 = new Thread(null, "Welcome to the forum", "This is the first thread", LocalDateTime.now(), user1, category1, new HashSet<>());
                Thread thread2 = new Thread(null, "Need help with Java", "I'm stuck with a problem", LocalDateTime.now(), user1, category2, new HashSet<>());

                em.persist(thread1);
                em.persist(thread2);

                // Create Posts
                Post post1 = new Post("This is a post in the first thread", user1, thread1, null, null);
                Post post2 = new Post("This is another post in the first thread", user1, thread1, null, null);
                Post post3 = new Post("This is a post in the second thread", user1, thread2, null, null);

                em.persist(post1);
                em.persist(post2);
                em.persist(post3);

                // Create Reply and associate with posts
                Reply reply1 = new Reply();
                em.persist(reply1);

                Post replyPost1 = new Post("This is a reply to the second post in the first thread", user1, thread1, reply1, null);
                em.persist(replyPost1);

                reply1.setParentPost(replyPost1);
                em.persist(reply1);

                // Add posts to threads
                thread1.getPosts().add(post1);
                thread1.getPosts().add(post2);
                thread1.getPosts().add(replyPost1);
                thread2.getPosts().add(post3);

                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
                e.printStackTrace();
            }
        }
    }
