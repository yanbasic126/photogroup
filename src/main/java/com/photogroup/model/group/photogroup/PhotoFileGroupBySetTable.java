package com.photogroup.model.group.photogroup;

import com.photogroup.model.group.AbstractGroupBySetTable;

public final class PhotoFileGroupBySetTable extends AbstractGroupBySetTable<PhotoFileModel> {

    @Override
    public Class<PhotoFileModel> getModelClass() {
        return PhotoFileModel.class;
    }

}
