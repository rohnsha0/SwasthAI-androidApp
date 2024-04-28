package com.rohnsha.medbuddyai.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.outlined.CloseFullscreen
import androidx.compose.material.icons.outlined.FeaturedPlayList
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material.icons.outlined.Quickreply
import androidx.compose.material.icons.outlined.ShortText
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material.icons.outlined.Transgender
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.rohnsha.medbuddyai.bottom_navbar.bottomNavItems
import com.rohnsha.medbuddyai.domain.viewmodels.communityVM
import com.rohnsha.medbuddyai.domain.viewmodels.snackBarToggleVM
import com.rohnsha.medbuddyai.ui.theme.BGMain
import com.rohnsha.medbuddyai.ui.theme.ViewDash
import com.rohnsha.medbuddyai.ui.theme.customBlue
import com.rohnsha.medbuddyai.ui.theme.customGreen
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
        communityViewModel.getFeed()
        val postData= communityViewModel.feedContents.collectAsState().value
        val replyData= communityViewModel.replyContents.collectAsState().value
        Log.d("dataSnapCOmmPost", postData.toString())
        Log.d("dataSnapCOmmReply", replyData.toString())
        val postsWithReplies = communityViewModel.mergePostReplies()
        Log.d("dataSnapCOmmPostReply", postsWithReplies.toString())

        val isCreateExpanded= remember {
            mutableStateOf(false)
        }
        val title= remember {
            mutableStateOf("")
        }
        val content= remember {
            mutableStateOf("")
        }
        val domainExpanded= remember {
            mutableStateOf(false)
        }
        val domain= remember {
            mutableIntStateOf(0)
        }
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
                        title = if (isCreateExpanded.value) "Post Now" else "Write",
                        icon = if (isCreateExpanded.value) Icons.Outlined.TaskAlt else Icons.Outlined.PostAdd,
                        weight = if (isCreateExpanded.value) .56f else .49f,
                        onClickListener = { 
                            if (isCreateExpanded.value) {
                                if (title.value!="" && content.value!="" && domain.intValue!=0){
                                    communityViewModel.post(
                                        title = title.value,
                                        content= content.value,
                                        domainIndex = domain.intValue,
                                        onCompleteLambda = {
                                            isCreateExpanded.value= false
                                            title.value= ""
                                            content.value= ""
                                            snackBarViewModel.SendToast(
                                                message = "Post was uploaded successfully",
                                                indicator_color = customGreen,
                                                padding = PaddingValues(2.dp)
                                            )
                                        }
                                    )
                                } else snackBarViewModel.SendToast(
                                    "One of more field is empty!",
                                    customRed,
                                    PaddingValues(0.dp)
                                )
                            } else isCreateExpanded.value= true

                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    explore_tabs(
                        title = if (isCreateExpanded.value) "Close" else "From Records",
                        icon = if (isCreateExpanded.value) Icons.Outlined.CloseFullscreen else Icons.Outlined.FeaturedPlayList,
                        weight = 1f,
                        onClickListener = {
                            if (isCreateExpanded.value) isCreateExpanded.value= false else snackBarViewModel.SendToast(
                                message = "The feature is still under making!",
                                indicator_color = Color.Red,
                                padding = PaddingValues(2.dp)
                        ) }
                    )
                }
                AnimatedVisibility (
                    visible = isCreateExpanded.value,
                ){
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        TextInputThemed(
                            value = title.value,
                            onValueChanged = { title.value= it },
                            label = "Enter title",
                            icon = Icons.Outlined.Title,
                            onClose = { title.value = "" },
                            isNumKey = false
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TextInputThemed(
                            value = content.value,
                            onValueChanged = { content.value= it },
                            label = "Enter contents",
                            icon = Icons.Outlined.ShortText,
                            isNumKey = false,
                            onClose = { content.value = "" },
                            singleLine = false
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ExposedDropdownMenuBox(
                            expanded = domainExpanded.value,
                            onExpandedChange = { domainExpanded.value = it },
                        ) {
                            TextInputThemed(
                                value = communityViewModel.returnDomain(domain.intValue),
                                onValueChanged = {  },
                                icon = when(domain.intValue){
                                    1 -> Icons.Outlined.Male
                                    2 -> Icons.Outlined.Female
                                    else -> Icons.Outlined.Transgender
                                },
                                label = "Select Domain",
                                onClose = {  },
                                isNumKey = true,
                                readOnly = true,
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = domainExpanded.value,
                                onDismissRequest = { domainExpanded.value = false },
                            ) {
                                DropdownMenuItem(text = { Text(text = "Lungs") }, onClick = {
                                    domain.intValue= 1
                                    domainExpanded.value = false
                                })
                                DropdownMenuItem(text = { Text(text = "Brain") }, onClick = {
                                    domain.intValue= 2
                                    domainExpanded.value = false
                                })
                            }
                        }
                    }
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
            if (postData.isNotEmpty()){
                items(postsWithReplies){ it ->
                    val data= it.post
                    CommunityPostItem(
                        title = data.title?: "Unknown",
                        subtitle = "by ${data.author}",
                        data = "${it.replies.size} replies",
                        additionData = data.timestamp?.let {
                            communityViewModel.calculateTimeDifference(
                                it.toLong())
                        }
                            ?: "Unspecified",
                        colorLogo = customBlue,
                        postData = data.content?: "Unknown",
                        onClickListener = {
                            it.post.id?.let { it1 -> Log.d("repliesID", it1) }
                            navController.navigate(bottomNavItems.CommunityReply.returnPostID(
                            it.post.id!!
                        )) }
                    )
                }
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
    imageVector: ImageVector= Icons.Outlined.Quickreply,
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
            .clickable {
                if (onClickListener != null) {
                    onClickListener()
                }
            },
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
                        text = title.clip(25),
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