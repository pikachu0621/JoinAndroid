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
    private File file; // 第一张图片
    private List<File> files; // 相册所有图片


    public PhotoModule(String path, String name, File file, List<File> files) {
        this.path = path;
        this.name = name;
        this.file = file;
        this.files = files;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
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
