package org.apache.diana.cassandra.document;


import org.apache.diana.api.column.Column;
import org.apache.diana.api.column.ColumnEntity;
import org.apache.diana.api.column.ColumnEntityManager;

import java.util.List;
import java.util.function.Consumer;

class CassandraDocumentEntityManager implements ColumnEntityManager {


    @Override
    public void save(ColumnEntity columnEntity) {

    }

    @Override
    public void saveAsync(ColumnEntity columnEntity) {

    }

    @Override
    public void saveAsync(ColumnEntity columnEntity, Consumer<Void> consumer) {

    }

    @Override
    public void update(ColumnEntity columnEntity) {

    }

    @Override
    public void updateAsync(ColumnEntity columnEntity) {

    }

    @Override
    public void updateAsync(ColumnEntity columnEntity, Consumer<Void> consumer) {

    }

    @Override
    public void delete(Column column) {

    }

    @Override
    public void deleteAsync(Column column) {

    }

    @Override
    public void deleteAsync(Column column, Consumer<Void> consumer) {

    }

    @Override
    public ColumnEntity find(Column column) {
        return null;
    }

    @Override
    public void findAsync(Column column, Consumer<ColumnEntity> consumer) {

    }

    @Override
    public List<ColumnEntity> nativeQuery(String s) {
        return null;
    }

    @Override
    public org.apache.diana.api.column.PreparedStatement nativeQueryPrepare(String s) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
