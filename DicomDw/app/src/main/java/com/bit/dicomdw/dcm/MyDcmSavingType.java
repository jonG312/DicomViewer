package com.bit.dicomdw.dcm;

import com.imebra.*;

public enum MyDcmSavingType {
    DICOM(codecType_t.dicom),JPEG(codecType_t.jpeg);

    private codecType_t type;

    private MyDcmSavingType(codecType_t type){
        this.type=type;
    }

    public codecType_t getType(){
        return this.type;
    }

}
