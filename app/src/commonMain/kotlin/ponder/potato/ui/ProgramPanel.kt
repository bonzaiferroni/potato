package ponder.potato.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ponder.potato.model.game.ExecutorState
import pondui.ui.controls.Button
import pondui.ui.controls.Column
import pondui.ui.controls.FlowRow
import pondui.ui.controls.H1
import pondui.ui.controls.Row
import pondui.ui.theme.Spacing
import pondui.utils.modifyIfTrue

@Composable
fun ProgramPanel(
    entityId: Long
) {
    InflateEntityWithDelta<ExecutorState>(entityId) { entity, game, delta ->

        var programId by remember(entityId) { mutableStateOf(entity.state.programId) }
        val program = game.programs[programId]

        LaunchedEffect(programId) {
            entity.state.programId = programId
            entity.state.instructionId = 0
        }

        if (program != null) {
            Row {
                H1(program.name)
                Spacer(modifier = Modifier.weight(1f))
                Button("Change") { programId = null }
            }
            val items = remember(programId) { program.statements.toInstructionItems(game) }
            Column(Spacing.Tight) {
                for (item in items) {
                    val isCurrentInstruction = entity.state.instructionId == item.id
                    InstructionItemView(
                        item = item,
                        modifier = Modifier.modifyIfTrue(isCurrentInstruction) { background(Color.Red.copy(.1f))}
                    )
                }
            }
        } else {
            val items = remember { game.programs.toProgramItems() }
            FlowRow(spacing = Spacing.Tight, modifier = Modifier.background(Color.Black.copy(.1f))) {
                for (item in items) {
                    Button(item.programName, onClick = { programId = item.programId })
                }
            }
        }
    }
}