package com.uptc.edu.boterito.repository;

import com.uptc.edu.boterito.model.ObraUrbanArt;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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

        MatchOperation matchValidado = Aggregation
                .match(Criteria.where("registeredStatus.estado_registro").is("validado"));

        // 3) Crea la Aggregation pipeline con los stages (orden importa)
        Aggregation aggregation = createAgregations(matchValidado);

        // 4) Ejecuta la agregación sobre la colección "obras" y mapea el resultado a
        // ObraUrbanArt.class
        AggregationResults<ObraUrbanArt> results = mongoTemplate.aggregate(aggregation, "obras", ObraUrbanArt.class);

        // 5) Retorna la lista mapeada
        return results.getMappedResults();
    }

    // 1) LookupOperation: crea el $lookup entre "obras" y "cualquier coleccion"
    // from -> colección destino (usuarios)
    // localField -> campo en "obras" que contiene el id del autor en la BD
    // ("autor_id" o "autorId" según tu mapeo)
    // foreignField -> campo _id en la colección "autores"
    // as -> nombre del campo resultado donde se guardará el autor (será un array)
    private LookupOperation createLookupOperation(String from, String localField, String foreignField, String name) {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from(from)
                .localField(localField) // debe coincidir exactamente con el nombre del campo en Mongo
                .foreignField(foreignField)
                .as(name);
        return lookupOperation;
    }

    private UnwindOperation createUnwindOperation(String field) {
        UnwindOperation unwin = Aggregation.unwind(field, true);
        return unwin;
    }

    private Aggregation createAgregations(MatchOperation matchOperation) {

        LookupOperation lookupIlustracion = createLookupOperation("ilustracion_muralista", "ilustracion_id", "_id",
                "ilustracion");
        LookupOperation lookupLocation = createLookupOperation("ubicaciones", "ubicaciones_id", "_id", "ubicacion");
        LookupOperation lookupTecnica = createLookupOperation("tecnicas", "tecnicas_id", "_id", "tecnica");
        LookupOperation lookupTipo = createLookupOperation("tipo_mural", "tipo_mural_id", "_id", "tipo");
        LookupOperation lookupEstadoConservacion = createLookupOperation("estado_conservacion",
                "estado_conservacion_id", "_id", "estadoConservacion");
        LookupOperation lookupTipografia = createLookupOperation("tipografias", "tipografias_id", "_id", "typography");
        LookupOperation lookupSuperficie = createLookupOperation("superficie", "superficie_id", "_id", "surface");
        LookupOperation lookupEstadoRegistro = createLookupOperation("estado_registro", "estado_registrado_id", "_id",
                "registeredStatus");
        // 2) UnwindOperation: convierte el array "autor" en un objeto "autor" (mantiene
        // obras sin autor si preserve true)

        UnwindOperation unwindIlustracion = createUnwindOperation("ilustracion");
        UnwindOperation unwindUbicacion = createUnwindOperation("ubicacion");
        UnwindOperation unwindTecnica = createUnwindOperation("tecnica");
        UnwindOperation unwindTipo = createUnwindOperation("tipo");
        UnwindOperation unwindEstadoConservacion = createUnwindOperation("estadoConservacion");
        UnwindOperation unwindTipografia = createUnwindOperation("typography");
        UnwindOperation unwindSuperficie = createUnwindOperation("surface");
        UnwindOperation unwindEstadoRegistro = createUnwindOperation("registeredStatus");

        List<AggregationOperation> operations = new ArrayList<>();

        

        // Agregas todos los lookups y unwinds
        operations.add(lookupIlustracion);
        operations.add(unwindIlustracion);
        operations.add(lookupLocation);
        operations.add(unwindUbicacion);
        operations.add(lookupTecnica);
        operations.add(unwindTecnica);
        operations.add(lookupTipo);
        operations.add(unwindTipo);
        operations.add(lookupEstadoConservacion);
        operations.add(unwindEstadoConservacion);
        operations.add(lookupTipografia);
        operations.add(unwindTipografia);
        operations.add(lookupSuperficie);
        operations.add(unwindSuperficie);
        operations.add(lookupEstadoRegistro);
        operations.add(unwindEstadoRegistro);

        if (matchOperation != null) {
            operations.add(matchOperation);
        }

        // Finalmente creas la agregación
        Aggregation aggregation = Aggregation.newAggregation(operations);

        return aggregation;
    }
}
