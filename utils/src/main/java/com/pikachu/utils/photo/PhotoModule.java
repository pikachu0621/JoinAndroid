package com.pikachu.utils.photo;

import java.io.File;
import java.util.List;

/**
 * @author pkpk.run
 * @project ps
 * @package com.pikachu.utils.photo
 * @date 2021/9/23
 * @description 略
 */
public class PhotoModule {

    private String path; // 相册路径 全
    private String name; // 相册名字
    private String file; // 第一张图片
    private List<String> files; // 相册所有图片


    public PhotoModule(String path, String name, String file, List<String> files) {
        this.path = path;
        this.name = name;
        this.file = file;
        this.files = files;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
