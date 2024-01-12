package com.rohnsha.medbuddyai

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Quickreply
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rohnsha.medbuddyai.domain.dataclass.communityFields
import com.rohnsha.medbuddyai.domain.viewmodels.communityVM
import com.rohnsha.medbuddyai.domain.viewmodels.snackBarToggleVM
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customRed
import com.rohnsha.medbuddyai.ui.theme.fontFamily
import com.rohnsha.medbuddyai.ui.theme.lightTextAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    padding: PaddingValues,
    navController: NavHostController,
    snackBarViewModel: snackBarToggleVM,
    communityViewModel: communityVM
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Community",
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),
                        fontSize = 26.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BGMain
                )
            )
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = BGMain
    ) { values ->
        val list= listOf(
            communityFields(
                "Rohan Shaw",
                "Related Cough",
                "12:00 AM",
                "I am having this weird issue with blah glah",
                "Heart"
                ),
            communityFields(
                "Imaginary Shaw",
                "Related Disease",
                "02:00 AM",
                "I am having this unknown rashes on the any organ to blahg lah",
                "Lungs"
            )
        )
        LazyColumn(
            modifier = Modifier
                .padding(values)
                .padding(padding)
                .padding(top = 20.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(top = 30.dp, start = 24.dp, end = 24.dp)
        ) {
            item {
                Text(
                    text = "Create",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    explore_tabs(
                        title = "Write",
                        icon = Icons.Filled.Create,
                        weight = .49f,
                        onClickListener = { communityViewModel.post() }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    explore_tabs(
                        title = "From Records",
                        icon = Icons.Filled.EditNote,
                        weight = 1f,
                        onClickListener = {
                            snackBarViewModel.SendToast(
                                message = "Hello, This is Test!",
                                indicator_color = Color.Red,
                                padding = PaddingValues(2.dp)
                        ) }
                    )
                }
                Text(
                    text = "Recent Posts",
                    fontFamily = fontFamily,
                    fontWeight = FontWeight(600),
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(bottom = 6.dp, top = 18.dp)
                )
            }
            items(list) { data ->
                CommunityPostItem(
                    title = data.title,
                    subtitle = "by ${data.user_name}",
                    data = data.domain,
                    additionData = "on ${data.timestamp}",
                    colorLogo = customRed,
                    postData = data.data
                )
            }
        }
    }
}

@Composable
fun CommunityPostItem(
    title: String,
    subtitle: String,
    data: String,
    additionData: String? =null,
    imageVector: ImageVector= Icons.Filled.Quickreply,
    colorLogo: Color,
    additionalDataColor: Color?= null,
    colorLogoTint: Color? = null,
    onClickListener: (() -> Unit)? =null,
    postData: String
) {
    Box(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .background(color = ViewDash, shape = RoundedCornerShape(16.dp))
            .clickable { },
        contentAlignment = Alignment.CenterStart
    ){
        Column {
            Row(
                modifier = Modifier
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = 13.dp)
                        .size(34.dp)
                        .background(colorLogo, CircleShape)
                        .padding(6.dp),
                    imageVector = imageVector,
                    contentDescription = "$imageVector icon",
                    colorFilter = ColorFilter.tint(colorLogoTint ?: Color.White)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 13.dp)
                ) {
                    Text(
                        modifier = Modifier,
                        text = title.clip(16),
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600),

                    )
                    Text(
                        modifier = Modifier
                            .offset(y = (-2).dp),
                        text = subtitle,
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        color = lightTextAccent
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 18.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        modifier = Modifier,
                        text = data,
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight(600)
                    )
                    additionData?.let {
                        Text(
                            modifier = Modifier
                                .offset(y = (-2).dp),
                            text = it,
                            fontSize = 14.sp,
                            fontFamily = fontFamily,
                            color = additionalDataColor ?: colorLogo,
                            fontWeight = FontWeight(600)
                        )
                    }
                }
            }
            Text(
                text = postData,
                fontSize = 14.sp,
                fontFamily = fontFamily,
                modifier = Modifier
                    .padding(start = 13.dp, end = 24.dp, bottom = 13.dp)
                    .fillMaxWidth()
            )
        }
    }
}

fun String.clip(
    maxChar: Int
): String{
    return if (this.length>=maxChar){
        "${this.substring(0, maxChar)}..."
    } else{
        this
    }
}