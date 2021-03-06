package org.scitokens.util;

import edu.uiuc.ncsa.myproxy.oa4mp.oauth2.storage.OA2TConverter;
import edu.uiuc.ncsa.security.core.IdentifiableProvider;
import edu.uiuc.ncsa.security.delegation.server.storage.ClientStore;
import edu.uiuc.ncsa.security.delegation.storage.Client;
import edu.uiuc.ncsa.security.delegation.token.TokenForge;
import edu.uiuc.ncsa.security.storage.data.ConversionMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <p>Created by Jeff Gaynor<br>
 * on 9/25/17 at  11:06 AM
 */
public class STTransactionConverter<V extends STTransaction> extends OA2TConverter<V> {
    public STTransactionConverter(STTransactionKeys keys,
                                  IdentifiableProvider<V> identifiableProvider,
                                  TokenForge tokenForge,
                                  ClientStore<? extends Client> cs) {
        super(keys, identifiableProvider, tokenForge, cs);
    }


    protected STTransactionKeys getStTransactionKeys() {
        return (STTransactionKeys) keys;
    }

    @Override
    public V fromMap(ConversionMap<String, Object> map, V v) {
        V st = super.fromMap(map, v);
        Object claims = map.get(getStTransactionKeys().sciTokens());
        if (claims != null) {
            if (claims instanceof JSONObject) {
                st.setClaims((JSONObject) claims);
            }
            if (claims instanceof String) {
                JSONObject json = JSONObject.fromObject(claims);
                st.setClaims(json);
            }
        }
        if (map.containsKey(getStTransactionKeys().stScopes())) {
            st.setStScopes(map.getString(getStTransactionKeys().stScopes()));
        }
        if (map.containsKey(getStTransactionKeys().audience())) {
            Object audience = map.get(getStTransactionKeys().audience());
            if (audience != null) {
                if (audience instanceof JSONArray) {
                    st.setAudience((JSONArray) audience);
                }
                if (audience instanceof String) {
                    JSONArray json = JSONArray.fromObject(audience);
                    st.setAudience(json);
                }
            }
        }
        return st;
    }

    @Override
    public void toMap(V t, ConversionMap<String, Object> map) {
        super.toMap(t, map);
        if (t.getClaims() != null) {
            map.put(getStTransactionKeys().sciTokens(), t.getClaims().toString());
        }
        if (t.getStScopes() != null) {
            map.put(getStTransactionKeys().stScopes(), t.getStScopes());
        }
        if(t.getAudience() != null){
            JSONArray jsonArray = null;
            if(t.getAudience() instanceof JSONArray){
                jsonArray  = (JSONArray) t.getAudience();
            }else{
                jsonArray = new JSONArray();
                jsonArray.addAll(t.getAudience());
            }
            map.put(getStTransactionKeys().audience(), jsonArray.toString());
        }
    }

}
