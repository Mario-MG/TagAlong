package com.hfad.tagalong.tools.db.room

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)

@Suite.SuiteClasses(
    TagDaoInsertTest::class,
    TagDaoInsertAllTest::class,
    TagDaoGetTagsForSongByIdTest::class,
    TagDaoDeleteByNameTest::class,
    SongDaoInsertTest::class,
    SongDaoInsertAllTest::class,
    SongDaoGetSongsWithAnyOfTheTagsByNameTest::class,
    SongDaoGetSongsWithAllOfTheTagsByNameTest::class,
    SongDaoDeleteByIdTest::class,
    SongTagCrossRefDaoInsertTest::class,
    SongTagCrossRefDaoInsertAllTest::class,
    SongTagCrossRefDaoDeleteTest::class,
    RuleDaoInsertTest::class,
    RuleDaoInsertAllTest::class,
    RuleDaoGetByIdWithTagsTest::class,
    RuleDaoGetAllWithTagsTest::class,
    RuleDaoDeleteByIdTest::class
)

internal class DaosTestSuite