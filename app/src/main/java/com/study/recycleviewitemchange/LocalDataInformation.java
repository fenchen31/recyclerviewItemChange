package com.study.recycleviewitemchange;

/**
 * @author      黎亮亮
 * @data        2021/3/26
 * @describe    RecyclerView的item数据模板
 * @attention   注：数据只是简单的两个String类型，但是为了方便在数据量比较大时能准确地
 *                  判断出使用对象，所以单独设计了一个类，并在构造方法中传入一个item中所需的
 *                  全部数据
 */
public class LocalDataInformation {

    private String title = "";
    private String content = "";

    public LocalDataInformation(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
