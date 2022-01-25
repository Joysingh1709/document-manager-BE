package com.documentmanager.api.models;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AccessLevel;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@ToString
public class ObjectUpload {
    @Getter
    @Setter
    private String userId;

    @Getter
    @Setter
    private String docName;

    @Getter
    @Setter
    private File file;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private String path;
}
