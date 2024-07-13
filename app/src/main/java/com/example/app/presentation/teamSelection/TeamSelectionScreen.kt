import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.app.presentation.shared.SharedViewModel
import com.example.app.presentation.teamSelection.TeamCard
import com.example.app.presentation.uiKit.SetStatusBarColor
import com.example.app.presentation.uiKit.getTeamColor

@Composable
fun TeamSelectionScreen(
    onNavigateToNextScreen: () -> Unit,
    sharedViewModel: SharedViewModel,
) {
    val selectedTeamIndex = rememberSaveable { mutableStateOf<Int?>(null) }
    val teams by sharedViewModel.teams.collectAsState()
    val screenColor = getTeamColor(teams.getOrNull(selectedTeamIndex.value ?: -1)?.id)

    SetStatusBarColor(color = screenColor)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp)
            .padding(bottom = 40.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 24.dp),
            style = MaterialTheme.typography.titleMedium,
            text = "Welcome! Please select a football club and proceed"
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(teams.size) { index ->
                TeamCard(
                    videoName = teams[index].teamName,
                    imageUrl = teams[index].imageUrl,
                    isSelected = index == selectedTeamIndex.value
                ) {
                    selectedTeamIndex.value = index
                }
            }
            item {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Button(
            modifier = Modifier.size(width = 250.dp, height = 60.dp),
            onClick = {
                sharedViewModel.selectTeam(selectedTeamIndex.value!!)
                onNavigateToNextScreen()
            },
            enabled = selectedTeamIndex.value != null,
            colors = ButtonDefaults.buttonColors(
                containerColor = screenColor ?: MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                fontSize = TextUnit(24f, TextUnitType.Sp),
                text = "Proceed"
            )
        }
    }
}