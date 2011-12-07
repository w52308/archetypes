#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.entity;


import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAbstractPersistable is a Querydsl query type for AbstractPersistable
 */
@Generated("com.mysema.query.codegen.SupertypeSerializer")
public class QAbstractPersistable extends EntityPathBase<AbstractPersistable<? extends java.io.Serializable>> {

    private static final long serialVersionUID = 697085746;

    public final SimplePath<java.io.Serializable> id = createSimple("id", java.io.Serializable.class);

    public QAbstractPersistable(Path<? extends AbstractPersistable<? extends java.io.Serializable>> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    @SuppressWarnings("unchecked")
    public QAbstractPersistable(PathMetadata<?> metadata) {
        super((Class)AbstractPersistable.class, metadata);
    }

}

