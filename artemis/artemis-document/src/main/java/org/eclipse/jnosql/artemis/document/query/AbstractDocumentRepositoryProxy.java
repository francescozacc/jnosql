/*
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.artemis.document.query;


import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.document.DocumentQueryPagination;
import jakarta.nosql.mapping.document.DocumentTemplate;
import org.eclipse.jnosql.artemis.query.RepositoryType;
import org.eclipse.jnosql.artemis.reflection.DynamicQueryMethodReturn;
import org.eclipse.jnosql.artemis.reflection.DynamicReturn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static jakarta.nosql.document.DocumentQuery.select;


/**
 * The template method to {@link jakarta.nosql.mapping.Repository} to Document
 *
 * @param <T> the class type
 */
public abstract class AbstractDocumentRepositoryProxy<T> extends BaseDocumentRepository implements InvocationHandler {


    protected abstract Repository getRepository();

    protected abstract DocumentTemplate getTemplate();

    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

        RepositoryType type = RepositoryType.of(method);
        Class<?> typeClass = getClassMapping().getClassInstance();

        switch (type) {
            case DEFAULT:
                return method.invoke(getRepository(), args);
            case FIND_BY:
                DocumentQuery query = getQuery(method, args);
                return executeQuery(method, args, typeClass, query);
            case FIND_ALL:
                DocumentQuery queryFindAll = select().from(getClassMapping().getName()).build();
                return executeQuery(method, args, typeClass, getQuerySorts(args, queryFindAll));
            case DELETE_BY:
                DocumentDeleteQuery documentDeleteQuery = getDeleteQuery(method, args);
                getTemplate().delete(documentDeleteQuery);
                return null;
            case OBJECT_METHOD:
                return method.invoke(this, args);
            case JNOSQL_QUERY:
                DynamicQueryMethodReturn methodReturn = DynamicQueryMethodReturn.builder()
                        .withArgs(args)
                        .withMethod(method)
                        .withTypeClass(typeClass)
                        .withPrepareConverter(q -> getTemplate().prepare(q))
                        .withQueryConverter(q -> getTemplate().query(q)).build();
                return methodReturn.execute();
            default:
                return Void.class;
        }
    }


    private Object executeQuery(Method method, Object[] args, Class<?> typeClass, DocumentQuery query) {
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(typeClass)
                .withMethodSource(method)
                .withResult(() -> getTemplate().select(query))
                .withSingleResult(() -> getTemplate().singleResult(query))
                .withPagination(DynamicReturn.findPagination(args))
                .withStreamPagination(listPagination(query))
                .withSingleResultPagination(getSingleResult(query))
                .withPage(getPage(query))
                .build();
        return dynamicReturn.execute();
    }

    private Function<Pagination, Page<T>> getPage(DocumentQuery query) {
        return p -> getTemplate().select(DocumentQueryPagination.of(query, p));
    }

    private Function<Pagination, Optional<T>> getSingleResult(DocumentQuery query) {
        return p -> {
            DocumentQuery queryPagination = DocumentQueryPagination.of(query, p);
            return getTemplate().singleResult(queryPagination);
        };
    }

    private Function<Pagination, Stream<T>> listPagination(DocumentQuery query) {
        return p -> {
            DocumentQuery queryPagination = DocumentQueryPagination.of(query, p);
            return getTemplate().select(queryPagination);
        };
    }
}
