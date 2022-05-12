package com.codan.cinder.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small.copy(CornerSize(size = 8.dp)),
    enabledColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    disabledColor: Color = MaterialTheme.colorScheme.surface,
    enabledTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    disabledTextColor: Color = MaterialTheme.colorScheme.onSurface,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    leadingIconVector: ImageVector? = null,
    trailingIconVector: ImageVector? = null,
    content: @Composable RowScope.() -> Unit
) {
    val color = if (enabled) enabledColor else disabledColor
    val textColor = if (enabled) enabledTextColor else disabledTextColor
    val borderStroke = if (enabled) null else BorderStroke(width = 1.dp, color = outlineColor)

    Surface(
        shape = shape,
        color = color,
        border = borderStroke,
        modifier = modifier
            .height(ChipHeight)
            .clickable(onClick = {}),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CompositionLocalProvider(LocalContentColor provides textColor) {
                if (leadingIconVector != null) {
                    Spacer(modifier = Modifier.width(LeadingIconStartSpacing))
                    Icon(
                        imageVector = leadingIconVector,
                        contentDescription = "Leading Chip Icon",
                        modifier = Modifier.size(LeadingIconSize)
                    )
                    Spacer(modifier = Modifier.width(LeadingIconEndSpacing))
                } else {
                    Spacer(modifier = Modifier.width(NoIconStartSpacing))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    content()
                }
                if (trailingIconVector != null) {
                    Spacer(modifier = Modifier.width(TrailingIconStartSpacing))
                    Icon(
                        imageVector = trailingIconVector,
                        contentDescription = "Leading Chip Icon",
                        modifier = Modifier.size(TrailingIconSize)
                    )
                    Spacer(modifier = Modifier.width(TrailingIconEndSpacing))
                } else {
                    Spacer(modifier = Modifier.width(NoIconEndSpacing))
                }
            }
        }
    }
}

private val ChipHeight = 32.dp
private val NoIconStartSpacing = 16.dp
private val NoIconEndSpacing = 16.dp
private val LeadingIconSize = 18.dp
private val TrailingIconSize = 18.dp
private val LeadingIconStartSpacing = 8.dp
private val LeadingIconEndSpacing = 8.dp
private val TrailingIconStartSpacing = 8.dp
private val TrailingIconEndSpacing = 8.dp
