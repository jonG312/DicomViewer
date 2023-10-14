package com.bit.dicomdw.dcm;

/**
 * MyDcmTag枚举类
 * 表示的是 DICOM格式的tag,这里只列了部分
 */
public enum MyDcmTag {
    /**patient tag*/
    //病人姓名
    PatientName("PatientName",0x10,0x10),
    //病人id
    PatientID("PatientID",0x10,0x20),
    //病人出生日期
    PatientBirthDate("PatientBirthDate",0x10,0x30),
    //病人出生时间
    PatientBirthTime("PatientBirthTime",0x10,0x32),
    //病人性别
    PatientSex("PatientSex",0x10,0x40),
    //病人体重
    PatientWeight("PatientWeight",0x10,0x1030),
    //病人怀孕状态
    PatientPregnancyState("PatientPregnancyState",0x10,0x21C0),

    /**study tag*/
    //检查号
    StudyAccessionNumber("StudyAccessionNumber",0x08,0x50),
    //检查id
    StudyID("StudyID",0x20,0x10),
    //检查实例id
    StudyInstanceUID("StudyInstanceUID",0x20,0x0D),
    //检查日期
    StudyDate("StudyDate",0x08,0x20),
    //检查时间
    StudyTime("StudyTime",0x08,0x30),
    //检查类型
    StudyModalities("StudyModalities",0x08,0x61),
    //检查的身体部位
    StudyBodyPart("StudyBoadyPart",0x08,0x15),
    //检查的描述
    StudyDescription("StudyDescription",0x08,0x1030),
    //检查时患者的年龄
    StudyPatientAge("StudyPatientAge",0x10,0x1010),

    /**series tag*/
    //序列号
    SeriesNumber("SeriesNumber",0x20,0x11),
    //序列id
    SeriesInstanceUID("SeriesInstanceUID",0x20,0x0E),
    //检查模态(MRI/CT/CR/DR)
    SeriesModality("SeriesModality",0x08,0x60),
    //序列说明
    SeriesDescription("SeriesDescription",0x08,0x103E),
    //序列日期
    SeriesDate("SeriesDate",0x08,0x21),
    //序列时间
    SeriesTime("SeriesTime",0x08,0x31),
    //身体部位
    SeriesBodyPart("SeriesBodyPart",0x18,0x15),

    /**image tag*/
    //拍摄日期
    ImageContentDate("ImageContentDate",0x08,0x23),
    //拍摄时间
    ImageContentTime("ImageContentTime",0x08,0x33),
    //图像码
    ImageNumber("ImageNumber",0x20,0x13),
    //图像采样率
    ImageSamplesPerPixel("ImageSamplesPerPixel",0x28,0x02),
    //图像总行数(行分辨率)
    ImageRows("ImageRows",0x28,0x10),
    //图像总列数(列分辨率)
    ImageColumns("ImageColumnx",0x28,0x11),
    //像素间距(像素中心间的物理距离)
    ImagePixelSpacing("ImagePixelSpacing",0x28,0x30),
    //分配的位数(存储每一个像素值时分配的位数，每一个样本应该拥有相同的这个值)
    ImageBitAllocated("ImageBitAllocated",0x28,0x100),
    //存储的位数：有12到16列举值(存储每一个像素用的位数.每一个样本应该有相同值)
    ImageBitStored("ImageBitStored",0x28,0x101),
    //高位
    ImageHighBit("ImageHighBit",0x28,0x102),

    /**others*/
    NumberOfFrames("NumberOfFrames",0x28,0x08),
    WindowCenter("WindowCenter",0x28,0x1050),
    WindowWidth("WindowWidth",0x28,0x1051),
    VOILUT("VOILUT",0x28,0x3010)
    ;

    //枚举名称
    private String name;
    //dicom图像tag的group部分,由一个4为16进制数表示(即16为数,可以用short存,但我选int)
    private int group;
    //dicom图像tag的element部分,由一个4为16进制数表示
    private int element;

    //枚举类的构造函数就是private的,所以再写private就多余了
    MyDcmTag(String name,int group,int element){
        this.name=name;
        this.group=group;
        this.element=element;
    }

    public String getName(){
        return this.name;
    }
    public int getGroup(){
        return this.group;
    }
    public int getElement(){
        return this.element;
    }
}
