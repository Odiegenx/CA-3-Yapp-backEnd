package restSecrurity.DOA.databaseDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import restSecrurity.DTO.ThreadDTO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.persistance.Role;
import restSecrurity.persistance.Thread;
import restSecrurity.persistance.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ThreadDAO extends DAO<Thread,Integer> {
    private static ThreadDAO instance;

    public ThreadDAO(boolean isTest) {
        super(Thread.class,false);
    }
    public static ThreadDAO getInstance(boolean isTest){
        if(instance == null){
            instance = new ThreadDAO(isTest);
        }
        return instance;
    }
    public List<ThreadDTO> getThreads(int count,int size) throws ApiException {
        try(EntityManager em = emf.createEntityManager()){
            List<ThreadDTO> threadDTOs = new ArrayList<>();
            String queryString = "SELECT t FROM Thread t";
            TypedQuery<Thread> query = em.createQuery(queryString, Thread.class);
            query.setFirstResult(count * size);
            query.setMaxResults(size);
            List<Thread> threads = query.getResultList();

            for (Thread thread : threads) {
                threadDTOs.add(new ThreadDTO(thread));
            }
            return threadDTOs;
        }catch (Exception e){
            throw new ApiException(500,"unable to get Threads");
        }
    }

    public List<ThreadDTO> getByCategory(String category) throws ApiException {
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Thread> query = em.createQuery("SELECT t FROM Thread t WHERE t.category.name = :category", Thread.class);
            query.setParameter("category", category);
            List<Thread> threads = query.getResultList();
            List<ThreadDTO> threadDTOs = threads.stream().map(ThreadDTO::new).toList();
            return threadDTOs;
        }catch (Exception e){
            throw new ApiException(500,"Error while getting threads by category: " + e.getMessage());
        }
    }

    public List<ThreadDTO> getByUserId(String id) throws ApiException{
        try(var em = emf.createEntityManager()) {
            TypedQuery<Thread> query = em.createQuery("SELECT t FROM Thread t WHERE t.user.id = :id ORDER BY t.createdDate DESC", Thread.class);
            query.setParameter("id", id);
            List<Thread> threads = query.getResultList();
            List<ThreadDTO> threadDTOS = threads.stream().map(ThreadDTO::new).toList();
            return threadDTOS;
        }catch(Exception e){
            throw new ApiException(500, "Error while getting threads by userID: "+e.getMessage());
        }
    }
    @Override
    public List<Thread> getAll() {
        try(EntityManager em = emf.createEntityManager()) {
            TypedQuery<Thread> query = em.createQuery("SELECT t FROM Thread t ORDER BY t.createdDate DESC", Thread.class);
            List<Thread> queryList = query.getResultList();
            return queryList;
        }
    }

}
