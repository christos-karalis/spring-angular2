package com.neurocom.crud.search.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.UriTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by c.karalis on 7/23/2015.
 */
public class ControllerUtils {
    private ControllerUtils() {
    }

    public static HttpHeaders generateLocationHeader(PersistentEntityResourceAssembler assembler, Object source) {
        HttpHeaders headers = new HttpHeaders();
        String selfLink = assembler.getSelfLinkFor(source).getHref();
        headers.setLocation(new UriTemplate(selfLink).expand());
        return headers;
    }

    public static Link getLinkSelf() {
        return new Link(new UriTemplate(ServletUriComponentsBuilder.fromCurrentRequest().build().toString()), Link.REL_SELF);
    }

    public static <T extends Object> Resources<T> getResources(Iterable<T> content) {
        return new Resources(content, getLinkSelf());
    }

    public static HttpEntity<Resources> getResults(Pageable pageable, CallableStatement statement, Integer size) throws SQLException {
        //Retrieve the returned result set
        ResultSet results = (ResultSet) statement.getObject(1);
        return getResults(pageable, size, results);
    }

    public static HttpEntity<Resources> getResults(Pageable pageable, Integer size, ResultSet results) throws SQLException {
        List<List<Object>> queryResults = new ArrayList<List<Object>>();

        //Get the number of columns of the current ResultSet
        int numOfResultSetColumns = results.getMetaData().getColumnCount();

        List<Object> list = new ArrayList<Object>();
        List<Object> headers = new ArrayList<Object>();
        //Fill the list of headers to be populated
        for (int i = 1; i <= numOfResultSetColumns; i++) {
            headers.add(results.getMetaData().getColumnName(i));
        }
        list.add(headers);

        int rowCounter = 0;

        while (results.next()) {
            //Since we populate the first column of the result set to GUI,
            //we need a check here to avoid null values
            if (results.getRow()>pageable.getPageNumber()*pageable.getPageSize())  {
                if (results.getObject(1) != null) {
                    List<Object> rowList = new LinkedList<Object>();
                    for (int i = 1; i <= numOfResultSetColumns; i++) {
                        rowList.add(results.getObject(i));
                    }
                    list.add(rowList);

                    if (++rowCounter >= pageable.getPageSize()) {
                        break;
                    }
                }
            }

        }
        if (size==null) {
            while (results.next()&&rowCounter<1000) {
                ++rowCounter;
            }
        } else {
            rowCounter = size;
        }
        queryResults.add(list);

        UriTemplate base = new UriTemplate(ServletUriComponentsBuilder.fromCurrentRequest().build().toString());
        int pages = rowCounter/pageable.getPageSize();
        if (rowCounter%pageable.getPageSize()!=0) {
            pages++;
        }
        PagedResources pagedResources = new PagedResources(queryResults, new PagedResources.PageMetadata(pageable.getPageSize(), pageable.getPageNumber(), rowCounter, pages), new Link(base, Link.REL_SELF));
        return new ResponseEntity<Resources>(pagedResources, HttpStatus.OK);
    }
}
