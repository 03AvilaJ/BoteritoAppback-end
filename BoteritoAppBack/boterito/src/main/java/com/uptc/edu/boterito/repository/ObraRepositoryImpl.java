package com.uptc.edu.boterito.repository;

import com.uptc.edu.boterito.model.ObraUrbanArt;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ObraRepositoryImpl implements ObraRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    // Constructor injection (mejor que @Autowired en campos)
    public ObraRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ObraUrbanArt> findAllWithAutor() {

        // 3) Crea la Aggregation pipeline con los stages (orden importa)
        Aggregation aggregation = createAgregations();

        // 4) Ejecuta la agregación sobre la colección "obras" y mapea el resultado a ObraUrbanArt.class
        AggregationResults<ObraUrbanArt> results =
                mongoTemplate.aggregate(aggregation, "obras", ObraUrbanArt.class);

        // 5) Retorna la lista mapeada
        return results.getMappedResults();
    }


    @Override
    public List<ObraUrbanArt> filterObra(String typefilter, String filter) {
        // Iniciamos el objeto Criteria vacío
        Criteria criteria = new Criteria();

        // Validamos que vengan datos válidos
        if (typefilter != null && !typefilter.isEmpty() &&
            filter != null && !filter.isEmpty()) {

            // Según el tipo de filtro que llegue, aplicamos el campo correcto
            switch (typefilter.toLowerCase()) {
                case "tipo":
                    // Filtra por el campo "tipo" directamente en la obra
                    criteria = Criteria.where("tipo.tipo_mural").is(filter);
                    break;

                case "ilustracion":
                    // Filtra dentro del subdocumento ilustracion, en el campo "tipo"
                    criteria = Criteria.where("ilustracion.nombre").is(filter);
                    break;

                case "tecnica":
                    // Filtra dentro del subdocumento tecnica, en el campo "nombre"
                    criteria = Criteria.where("tecnica.nombre").is(filter);
                    break;

                default:
                    // Si llega un filtro desconocido, no aplica condición (devuelve todo)
                    criteria = new Criteria();
                    break;
            }
        }

        // Creamos la consulta con el criterio definido
        Aggregation aggregation = createAgregations();
        Aggregation.match(criteria); // aquí aplicas el filtro

        AggregationResults<ObraUrbanArt> results =
                mongoTemplate.aggregate(aggregation, "obras", ObraUrbanArt.class);

        return results.getMappedResults();

    }


    // 1) LookupOperation: crea el $lookup entre "obras" y "cualquier coleccion"
    //    from   -> colección destino (usuarios)
    //    localField -> campo en "obras" que contiene el id del autor en la BD ("autor_id" o "autorId" según tu mapeo)
    //    foreignField -> campo _id en la colección "autores"
    //    as -> nombre del campo resultado donde se guardará el autor (será un array)
    private LookupOperation createLookupOperation(String from, String localField, String foreignField, String name){
         LookupOperation lookupOperation = LookupOperation.newLookup()
                .from(from)
                .localField(localField)   // debe coincidir exactamente con el nombre del campo en Mongo
                .foreignField(foreignField)
                .as(name);
        return lookupOperation;
    }
    private UnwindOperation createUnwindOperation(String field){
        UnwindOperation unwin = Aggregation.unwind(field, true);
        return unwin;
    }

    private Aggregation createAgregations(){
        LookupOperation lookupAutor = createLookupOperation("usuarios", "autor_id", "_id", "autor");
        LookupOperation lookupIlustracion = createLookupOperation("ilustracion_muralista", "ilustracion_id", "_id", "ilustracion");        
        LookupOperation lookupLocation = createLookupOperation("ubicaciones", "ubicaciones_id", "_id", "ubicacion");
        LookupOperation lookupTecnica = createLookupOperation("tecnicas", "tecnicas_id", "_id", "tecnica");
        LookupOperation lookupTipo = createLookupOperation("tipo_mural", "tipo_mural_id", "_id", "tipo");
        
        // 2) UnwindOperation: convierte el array "autor" en un objeto "autor" (mantiene obras sin autor si preserve true)
        UnwindOperation unwindAutor = createUnwindOperation("autor");
        UnwindOperation unwindIlustracion = createUnwindOperation("ilustracion");
        UnwindOperation unwindUbicacion = createUnwindOperation("ubicacion");
        UnwindOperation unwindTecnica = createUnwindOperation("tecnica");
        UnwindOperation unwindTipo = createUnwindOperation("tipo");

        // 3) Crea la Aggregation pipeline con los stages (orden importa)
        Aggregation aggregation = Aggregation.newAggregation(
                lookupAutor,
                unwindAutor,
                lookupIlustracion,
                unwindIlustracion,
                lookupLocation,
                unwindUbicacion,
                lookupTecnica,
                unwindTecnica,
                lookupTipo,
                unwindTipo
        );
        return aggregation;
    }
}
