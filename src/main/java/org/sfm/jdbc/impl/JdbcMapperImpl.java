package org.sfm.jdbc.impl;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
//IFJAVA8_START
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.Spliterator;
import java.util.Spliterators;
//IFJAVA8_END

import org.sfm.csv.parser.CellConsumer;
import org.sfm.csv.parser.CsvReader;
import org.sfm.jdbc.JdbcMapper;
import org.sfm.map.MappingException;
import org.sfm.map.RowHandlerErrorHandler;
import org.sfm.map.impl.FieldMapper;
import org.sfm.map.impl.MapperImpl;
import org.sfm.reflect.Instantiator;
import org.sfm.utils.RowHandler;

public final class JdbcMapperImpl<T> extends MapperImpl<ResultSet, T> implements JdbcMapper<T> {

	
	private final RowHandlerErrorHandler errorHandler; 
	
	public JdbcMapperImpl(final FieldMapper<ResultSet, T>[] mappers, final Instantiator<ResultSet, T> instantiator, final RowHandlerErrorHandler errorHandler) {
		super(mappers, instantiator);
		this.errorHandler = errorHandler;
	}

	@Override
	public <H extends RowHandler<T>> H forEach(final ResultSet rs, final H handler)
			throws SQLException, MappingException {
		while(rs.next()) {
			T t = map(rs);
			try {
				handler.handle(t);
			} catch(Throwable error) {
				errorHandler.handlerError(error, t);
			}
		}
		return handler;
	}

	@Override
	public Iterator<T> iterate(ResultSet rs) throws SQLException,
			MappingException {
		return new ResultSetIterator<T>(rs, this);
	}

	//IFJAVA8_START
	@Override
	public Stream<T> stream(ResultSet rs) throws SQLException, MappingException {
		return StreamSupport.stream(new JdbcSpliterator(rs), false);
	}

	public class JdbcSpliterator implements Spliterator<T> {
		private final ResultSet resultSet;

		public JdbcSpliterator(ResultSet resultSet) {
			this.resultSet = resultSet;
		}

		@Override
		public boolean tryAdvance(Consumer<? super T> action) {
			try {
				if (resultSet.next()) {
                    action.accept(map(resultSet));
                    return true;
                }
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			return false;
		}

		@Override
		public void forEachRemaining(Consumer<? super T> action) {
			try {
				forEach(resultSet, new RowHandler<T>() {
                    @Override
                    public void handle(T t) throws Exception {
                        action.accept(t);
                    }
                });
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public Spliterator<T> trySplit() {
			return null;
		}

		@Override
		public long estimateSize() {
			return Long.MAX_VALUE;
		}

		@Override
		public int characteristics() {
			return Spliterator.ORDERED | Spliterator.NONNULL;
		}
	}

	//IFJAVA8_END
}
