#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.entity;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SetPath;
import com.mysema.query.types.path.StringPath;

/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

	private static final long serialVersionUID = 1761432837;

	public static final QUser user = new QUser("user");

	public final org.springframework.data.jpa.domain.QAbstractPersistable _super = new org.springframework.data.jpa.domain.QAbstractPersistable(
			this);

	public final SetPath<Address, QAddress> addresses = this.<Address, QAddress> createSet("addresses", Address.class,
			QAddress.class);

	public final DateTimePath<java.util.Date> createDate = createDateTime("createDate", java.util.Date.class);

	public final StringPath email = createString("email");

	public final BooleanPath enabled = createBoolean("enabled");

	public final StringPath firstName = createString("firstName");

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final StringPath locale = createString("locale");

	public final StringPath name = createString("name");

	public final StringPath openId = createString("openId");

	public final StringPath passwordHash = createString("passwordHash");

	public final SetPath<Role, QRole> roles = this.<Role, QRole> createSet("roles", Role.class, QRole.class);

	public final StringPath userName = createString("userName");

	public QUser(String variable) {
		super(User.class, forVariable(variable));
	}

	public QUser(Path<? extends User> entity) {
		super(entity.getType(), entity.getMetadata());
	}

	public QUser(PathMetadata<?> metadata) {
		super(User.class, metadata);
	}

}
