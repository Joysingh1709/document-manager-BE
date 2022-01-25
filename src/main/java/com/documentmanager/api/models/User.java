package com.documentmanager.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.google.cloud.Timestamp;

import lombok.AccessLevel;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@ToString
@Getter
@Setter
public class User {

    private String userId;

    private String docTreeRefId;

    private String name;

    private String email;

    private String pictureUrl;

    private Long docsCount;

    private Timestamp lastUpdatedAt;

    private Timestamp createdAt;

    private Timestamp lastLoginAt;

}
