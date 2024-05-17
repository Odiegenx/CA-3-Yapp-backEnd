package restSecrurity.DOA.memoryDAO;

import io.javalin.http.HttpStatus;
import restSecrurity.DOA.iDAO;
import restSecrurity.exceptions.ApiException;
import restSecrurity.model.iModel;
import utills.Updater;

import java.util.ArrayList;
import java.util.List;

public abstract class MemoryDAO<T extends iModel<Integer>,D> implements iDAO<T,D> {

    /*
    This DAO only works if the DTO/model's id is of the data type Integer
    if you want to set the id to something else, String for example. you need to update
    the iModel interface with the datatype you want.
     */

    Class<T> objectClass;

    private List<T> memoryStorage;
    private Integer idCounter;

    public MemoryDAO(Class<T> tClass){
        this.objectClass = tClass;
        this.memoryStorage = new ArrayList<>();
        idCounter = 1;
    }

    @Override
    public T getById(D id) throws ApiException {
        T toFind = null;
        if(memoryStorage.isEmpty()) {
            throw new ApiException(404, "Not Found");
        }
        if(Integer.parseInt(String.valueOf(id)) > idCounter){
            throw new ApiException(400, "Bad Request");
        }
        toFind = memoryStorage.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
        if(toFind == null){
            throw new ApiException(HttpStatus.forStatus(404).getCode(),"No nothing with id" +id+" was found");
        }
        return toFind;
    }

    @Override
    public T create(T t) throws ApiException {
        if(!memoryStorage.contains(t)){
            /*
            Other way of counting id.
            int Id = memoryStorage.stream()
                    .mapToInt(x -> x.getId())
                    .max()
                    .orElse(0) + 1;
             */
            t.setId(idCounter);
            memoryStorage.add(t);
            idCounter++;
            return t;
        }else {
            throw new ApiException(422,"Duplicate Entry");
        }
    }

    @Override
    public T update(T t, D id) throws ApiException {
        if(Integer.parseInt(String.valueOf(id)) <= idCounter){
            T found = memoryStorage.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
            if(found != null){
                T foundUpdated = Updater.updateFromDTO(found, t);
                memoryStorage.remove(found);
                memoryStorage.add(foundUpdated);
                return foundUpdated;
            }else {
                throw new ApiException(404,"No nothing with id" +id+" was found");
            }
        }else {
            throw new ApiException(400,"Bad Request");
        }
    }

    @Override
    public T delete(D id) throws ApiException {
        T toDelete = null;
        if(id != null){
            toDelete = memoryStorage.stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
            if(toDelete != null){
                if(memoryStorage.remove(toDelete)) {
                    return toDelete;
                }else{
                    throw new ApiException(500,"System was unable to delete");
                }
            }else{
                throw new ApiException(404,"No nothing with id" +id+" was found");
            }
        }else{
            throw new ApiException(400,"Bad Request");
        }
    }

    @Override
    public List<T> getAll() {
        return memoryStorage;
    }
}
