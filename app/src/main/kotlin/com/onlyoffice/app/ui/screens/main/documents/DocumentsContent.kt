package com.onlyoffice.app.ui.screens.main.documents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Source
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.onlyoffice.app.R
import com.onlyoffice.domain.model.docspace.DocumentsData
import com.onlyoffice.domain.model.docspace.File
import com.onlyoffice.domain.model.docspace.Folder

@Composable
fun DocumentsContent(
    documentsData: DocumentsData,
    contentPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier,
    onClickToFolder: ((Folder) -> Unit)? = null,
    onClickToFile: ((File) -> Unit)? = null
) {
    val layoutDirection = LocalLayoutDirection.current
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding()
        ),
        modifier = modifier
    ) {
        items(documentsData.folders, key = { "Folder #${it.id}" }) { folder ->
            ListItem(
                headlineContent = {
                    Text(
                        text = folder.title ?: stringResource(R.string.untitled_folder),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = when (folder.foldersCount + folder.filesCount) {
                            0 -> Icons.Outlined.Folder
                            else -> Icons.Outlined.Source
                        },
                        contentDescription = "Folder",
                        tint = contentColorFor(LocalContentColor.current),
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(LocalContentColor.current)
                            .padding(8.dp)
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                    headlineColor = LocalContentColor.current,
                    leadingIconColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .clickable(enabled = onClickToFolder != null) {
                        onClickToFolder?.invoke(folder)
                    }
                    .padding(
                        top = 16.dp,
                        start = 16.dp + contentPadding
                            .calculateStartPadding(layoutDirection),
                        end = 16.dp + contentPadding
                            .calculateEndPadding(layoutDirection)
                    )
                    .fillMaxWidth()
            )
            HorizontalDivider()
        }


        items(documentsData.files, key = { "File #${it.id}" }) { file ->
            ListItem(
                headlineContent = {
                    Text(
                        text = file.title ?: stringResource(R.string.untitled_file),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Outlined.Description,
                        contentDescription = "File",
                        tint = contentColorFor(LocalContentColor.current),
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(LocalContentColor.current)
                            .padding(8.dp)
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                    headlineColor = LocalContentColor.current,
                    leadingIconColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .clickable(enabled = onClickToFile != null) {
                        onClickToFile?.invoke(file)
                    }
                    .padding(
                        top = 16.dp,
                        start = 16.dp + contentPadding
                            .calculateStartPadding(layoutDirection),
                        end = 16.dp + contentPadding
                            .calculateEndPadding(layoutDirection)
                    )
                    .fillMaxWidth()
            )
            HorizontalDivider()
        }
    }
}