package com.example.enebronotes.data.folder

/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import kotlinx.coroutines.flow.Flow

class OfflineFoldersRepository(private val folderDao: FolderDao) : FolderRepository {
    override fun getAllFoldersStream(): Flow<List<Folder>> = folderDao.getAllFolders()

    override fun getFolderStream(id: Long): Flow<Folder?> = folderDao.getFolder(id)

    override suspend fun insertFolder(item: Folder) = folderDao.insert(item)

    override suspend fun deleteFolder(item: Folder) = folderDao.delete(item)

    override suspend fun updateFolder(item: Folder) = folderDao.update(item)
}
