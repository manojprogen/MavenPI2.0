/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author progen
 */
public class SearchResBundleSqlServer extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"addSearchValues", "insert into PRG_USER_SEARCH_DETAILS(User_id,search_text,created_by,updated_by,create_date,update_date,search_count) values(&,'&',&,&,getdate(),getdate(),&)"}, {"addFavSearchValues", "insert into prg_user_fav_search(SEARCH_ID,USER_ID,CREATED_BY,UPDATED_BY,CREATE_DATE,UPDATE_DATE,SEARCHNAME) values(&,&,&,&,getdate(),getdate(),'&')"}, {"isSearchPresent", "select SEARCH_ID,search_count from PRG_USER_SEARCH_DETAILS where SEARCH_TEXT='&'"}, {"updateSearchCount", "update PRG_USER_SEARCH_DETAILS set SEARCH_COUNT=SEARCH_COUNT+1 where SEARCH_ID=&"}, {"checkFavoriteSearchName", "select SEARCH_FAV_ID from PRG_USER_FAV_SEARCH where  Lower(SEARCHNAME) like '&'"}
    };
}
