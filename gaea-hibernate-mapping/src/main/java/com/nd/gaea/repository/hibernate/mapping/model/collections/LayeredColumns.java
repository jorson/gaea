package com.nd.gaea.repository.hibernate.mapping.model.collections;

import com.nd.gaea.repository.hibernate.mapping.model.ColumnMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static com.nd.gaea.repository.hibernate.mapping.model.EqualityExtensions.*;
import static com.nd.gaea.repository.hibernate.mapping.model.EqualityExtensions.contentEquals;

/**
 *
 * @author jorson.WHY
 * @package com.nd.demo.mapping.model.collections
 * @since 2015-03-26
 */
public class LayeredColumns {

    private final LayeredValues layeredValues = new LayeredValues();


    public Collection<ColumnMapping> getColumns() {
        List<ColumnMapping> returnValues = new ArrayList<ColumnMapping>();

        if(layeredValues.size() == 0) {
            return returnValues;
        }
        Integer maxLayer = layeredValues.getMaxKey();
        HashSet<ColumnMapping>  values = (HashSet < ColumnMapping >)layeredValues.get(maxLayer);


        for(ColumnMapping value : values) {
            if(value == null){
                break;
            }
            returnValues.add(value);
        }
        return returnValues;
    }

    public void addColumn(int layer, ColumnMapping mapping) {
        if(!layeredValues.containsKey(layer)) {
            layeredValues.put(layer, new HashSet<ColumnMapping>());
        }
        ((HashSet<ColumnMapping>)layeredValues.get(layer)).add(mapping);
    }

    public void makeColumnsEmpty(int layer) {
        layeredValues.put(layer, new HashSet<ColumnMapping>());
    }

    public boolean makeSureContentEquals(LayeredColumns columns) {
        return contentEquals(layeredValues, columns.layeredValues);
    }
}
