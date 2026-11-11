package com.zeroday.cyberprotocol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val pythonTokenPattern = Regex(
    "\"""[\\s\\S]*?\"\"\"|'''[\\s\\S]*?'''|\\\"(?:\\\\.|[^\\\"\\\\])*\\\"|'(?:\\\\.|[^'\\\\])*'|#[^\\n]*|\\b(?:def|class|if|elif|else|for|while|in|and|or|not|is|return|import|from|as|try|except|finally|with|pass|break|continue|True|False|None|lambda|yield|async|await|raise|with|global|nonlocal)\\b|\\b\\d+(?:\\.\\d+)?\\b"
)

private fun highlightedPython(source: String) = buildAnnotatedString {
    append(source)
    pythonTokenPattern.findAll(source).forEach { match ->
        val token = match.value
        val color = when {
            token.startsWith("#") -> androidx.compose.ui.graphics.Color(0xFF718096)
            token.startsWith("\"") || token.startsWith("'") ->
                androidx.compose.ui.graphics.Color(0xFF9BD68A)
            token.firstOrNull()?.isDigit() == true ->
                androidx.compose.ui.graphics.Color(0xFFF2C879)
            else -> androidx.compose.ui.graphics.Color(0xFFC69BFF)
        }
        addStyle(SpanStyle(color = color), match.range.first, match.range.last + 1)
    }
}

private fun insertIndent(value: TextFieldValue): TextFieldValue {
    val start = value.selection.min
    val end = value.selection.max
    val insertion = "    "
    val updated = value.text.replaceRange(start, end, insertion)
    return value.copy(text = updated, selection = TextRange(start + insertion.length))
}

private fun moveCursor(value: TextFieldValue, direction: String): TextFieldValue {
    val text = value.text
    val cursor = value.selection.end.coerceIn(0, text.length)
    val target = when (direction) {
        "left" -> (cursor - 1).coerceAtLeast(0)
        "right" -> (cursor + 1).coerceAtMost(text.length)
        "up", "down" -> {
            val lineStart = text.lastIndexOf('\\n', (cursor - 1).coerceAtLeast(0)) + 1
            val column = cursor - lineStart
            val currentLineEnd = text.indexOf('\\n', cursor).let { if (it < 0) text.length else it }
            if (direction == "up") {
                if (lineStart == 0) cursor
                else {
                    val previousEnd = lineStart - 1
                    val previousStart = text.lastIndexOf('\\n', (previousEnd - 1).coerceAtLeast(0)) + 1
                    (previousStart + column).coerceAtMost(previousEnd)
                }
            } else {
                if (currentLineEnd == text.length) cursor
                else {
                    val nextStart = currentLineEnd + 1
                    val nextEnd = text.indexOf('\\n', nextStart).let { if (it < 0) text.length else it }
                    (nextStart + column).coerceAtMost(nextEnd)
                }
            }
        }
        else -> cursor
    }
    return value.copy(selection = TextRange(target))
}

@Composable
fun CodeEditor(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    minHeight: Int = 210
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(label, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            TextButton(onClick = { onValueChange(insertIndent(value)) }) { Text("TAB ↹") }
            TextButton(onClick = { onValueChange(moveCursor(value, "left")) }) { Text("←") }
            TextButton(onClick = { onValueChange(moveCursor(value, "down")) }) { Text("↓") }
            TextButton(onClick = { onValueChange(moveCursor(value, "up")) }) { Text("↑") }
            TextButton(onClick = { onValueChange(moveCursor(value, "right")) }) { Text("→") }
        }
        BasicTextField(
            value = value,
            onValueChange = { incoming ->
                val cursor = incoming.selection.start
                val justInsertedNewline = incoming.selection.collapsed &&
                    incoming.text.length == value.text.length + 1 &&
                    cursor > 0 && incoming.text.getOrNull(cursor - 1) == '\\n'
                if (justInsertedNewline) {
                    val previousLine = incoming.text.substring(0, cursor - 1).substringAfterLast('\\n')
                    val baseIndent = previousLine.takeWhile { it == ' ' || it == '\\t' }
                    val extraIndent = if (previousLine.trimEnd().endsWith(":")) "    " else ""
                    val indent = baseIndent + extraIndent
                    val updated = incoming.text.substring(0, cursor) + indent + incoming.text.substring(cursor)
                    onValueChange(incoming.copy(text = updated, selection = TextRange(cursor + indent.length)))
                } else {
                    onValueChange(incoming)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight.dp)
                .background(androidx.compose.ui.graphics.Color(0xFF101923), RoundedCornerShape(12.dp))
                .padding(12.dp)
                .onPreviewKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown && event.key == Key.Tab) {
                        onValueChange(insertIndent(value))
                        true
                    } else false
                },
            textStyle = TextStyle(
                color = androidx.compose.ui.graphics.Color(0xFFD7E2EA),
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                lineHeight = 19.sp
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            visualTransformation = VisualTransformation { text ->
                TransformedText(highlightedPython(text.text), OffsetMapping.Identity)
            }
        )
    }
}
