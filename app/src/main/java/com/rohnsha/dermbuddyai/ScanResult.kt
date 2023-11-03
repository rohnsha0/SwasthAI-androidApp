package com.rohnsha.dermbuddyai

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Dangerous
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rohnsha.dermbuddyai.api.disease_data_dataClass
import com.rohnsha.dermbuddyai.api.results_obj.resultsAPIService
import com.rohnsha.dermbuddyai.ui.theme.BGMain
import com.rohnsha.dermbuddyai.ui.theme.ViewDash
import com.rohnsha.dermbuddyai.ui.theme.fontFamily
import com.rohnsha.dermbuddyai.ui.theme.formAccent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

private lateinit var disease_results: disease_data_dataClass
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    padding: PaddingValues,
    group: String,
    serial_number: String,
) {

    val dynamicURL= "https://quuicqg435fkhjzpkawkhg4exi0vjslb.lambda-url.ap-south-1.on.aws/disease_data/$group/$serial_number"
    var isAPIcalling= remember {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = isAPIcalling){
        try {
            disease_results= resultsAPIService.getDiseaseData(dynamicURL)
            isAPIcalling.value= false
            Log.d("notablesInner", disease_results.toString())
        } catch (e: Exception){
            Log.e("error", e.printStackTrace().toString())
        }
    }
    Scaffold(
        topBar = {
            if (!isAPIcalling.value){
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = disease_results.disease_name,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight(600),
                                fontSize = 26.sp,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = disease_results.domain,
                                fontFamily = fontFamily,
                                color = Color.Black.copy(.75f),
                                fontSize = 13.sp,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = BGMain
                    )
                )
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        containerColor = BGMain
    ) { values ->
        if (isAPIcalling.value){
            LoadingLayout(
                titleList = listOf(
                    "Charting a Course to Health Discovery", "Preparing to Enlighten and Empower",
                    "Loading the Canvas of Your Health Journey", "Awaiting Your Arrival in the Health Universe",
                    "Elevating Your Health IQ One Byte at a Time", "Stepping into the Digital Library of Wellness",
                    "Welcome to the Wonderland of Health Wisdom", "Fueling Curiosity for a Healthier Tomorrow"

                ))
        }else{
            ScanResultsSuccess(padding = padding, values = values)
        }
    }
}

@Composable
fun LoadingLayout(
    titleList: List<String>
) {
    val random = Random(System.currentTimeMillis())
    val title= titleList[random.nextInt(0, (titleList.size-1))]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BGMain)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.95f)
        ){

        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            Alignment.BottomCenter
        ){
            Text(
                modifier = Modifier.padding(bottom = 20.dp),
                text = title,
                fontWeight = FontWeight(600),
                fontFamily = fontFamily,
                color = formAccent,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ScanResultsSuccess(
    padding: PaddingValues,
    values: PaddingValues,
) {
    val scrollState= rememberScrollState(0)
    Column(
        modifier = Modifier
            .padding(values)
            .padding(padding)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment= Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
            ){
                val imgURL= "https://uc9dd619a7914cc058a868562105.previews.dropboxusercontent.com/p/thumb/ACEegjVf-EgagMTAovx7WQLoxcf_o8Uuc__EWjvLNq5XCAFoiH4ataXZjSeNN9NTwPQfgIGMMJni8KrHTd68Uldd4RmI8OZmQM3nivwYPrA14tDmeolknp9xlW5xU69mhjzyxF45Ow5EKwLFx2KpqU9-43TT6ovsZt7NpJF99ooqlv0tVz_UV74ufFgqA8S6JWeDwOC1akn2JogklE3zOdg1cSVFtJP-uAxA5u6YRbWAx4kLlDk_zGOan5i_IUokL9CgIjEQRaFXLc5Anqy6c2GEmpDh-lPLp-afVsRUGZOD3rwC8K_ZkiB_1DlxOaZz5lujR528ovHlogZ9GysDxwacwZzNTXVcCJpTEGFaebArL9LQ2gPwgccJXayDM78LhBA/p.jpeg"
                AsyncImage(
                    model = imgURL,
                    contentScale = ContentScale.Crop,
                    contentDescription = "null",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Spacer(
                modifier = Modifier.height(24.dp)
            )
            OptionScanResults()
            Spacer(
                modifier = Modifier.height(30.dp)
            )
            DataBox(
                title = "Know About Disease",
                data = "Pneumonia is an infection that inflames the air sacs in one or both lungs. The air sacs may fill with fluid or pus (purulent material), causing cough with phlegm or pus, fever, chills, and difficulty breathing. A variety of organisms, including bacteria, viruses and fungi, can cause pneumonia. Pneumonia can range in seriousness from mild to life-threatening. It is most serious for infants and young children, people older than age 65, and people with health problems or weakened immune systems."
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            DataBox(
                title = "Symptoms",
                data = "The signs and symptoms of pneumonia vary from mild to severe, depending on factors such as the type of germ causing the infection, and your age and overall health. Mild signs and symptoms often are similar to those of a cold or flu, but they last longer.\n" +
                        "Signs and symptoms of pneumonia may include:" +
                        "\n" +
                        "1. Chest pain when you breathe or cough\n" +
                        "2. Confusion or changes in mental awareness (in adults age 65 and older)\n" +
                        "3. Cough, which may produce phlegm\n" +
                        "4. Fatigue\n" +
                        "5. Fever, sweating and shaking chills\n" +
                        "6. Lower than normal body temperature (in adults older than age 65 and people with weak immune systems)\n" +
                        "7. Nausea, vomiting or diarrhea\n" +
                        "8. Shortness of breath\n" +
                        "Newborns and infants may not show any sign of the infection. Or they may vomit, have a fever and cough, appear restless or tired and without energy, or have difficulty breathing and eating."
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            DataBox(
                title = "When to see doctor",
                data = "See your doctor if you have difficulty breathing, chest pain, persistent fever of 102 F (39 C) or higher, or persistent cough, especially if you're coughing up pus.\n" +
                        "It's especially important that people in these high-risk groups see a doctor:\n" +
                        "Adults older than age 65\n" +
                        "Children younger than age 2 with signs and symptoms\n" +
                        "People with an underlying health condition or weakened immune system\n" +
                        "People receiving chemotherapy or taking medication that suppresses the immune system\n" +
                        "For some older adults and people with heart failure or chronic lung problems, pneumonia can quickly become a life-threatening condition."
            )
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
fun OptionScanResults() {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OptionsScanResultUNI(
            title = "Chatbot",
            icon = Icons.Outlined.Chat,
            onClickListener = {}
        )
        OptionsScanResultUNI(
            title = "Post",
            icon = Icons.Outlined.Forum,
            onClickListener = {}
        )
        OptionsScanResultUNI(
            title = "Incorrect",
            icon = Icons.Outlined.Dangerous,
            onClickListener = {}
        )
        OptionsScanResultUNI(
            title = "Share",
            icon = Icons.Outlined.Share,
            onClickListener = {}
        )
    }
}

@Composable
fun OptionsScanResultUNI(
    title: String,
    icon:ImageVector,
    onClickListener: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(49.dp)
                .height(49.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                .background(ViewDash, shape = RoundedCornerShape(8.dp))
                .clickable(
                    onClick = onClickListener
                ),
            contentAlignment = Alignment.Center
        ){
            Image(
                imageVector =icon,
                contentDescription = null,
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight(400)
        )
    }
}

@Composable
fun DataBox(
    title: String,
    data: String
) {
    Column {
        Text(
            text = title,
            fontFamily = fontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier
                .padding(start = 25.dp)
        )
        Spacer(
            modifier = Modifier.height(14.dp)
        )
        Text(
            text = data,
            color = Color.Black,
            fontFamily = fontFamily,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(horizontal = 25.dp),
            textAlign = TextAlign.Start
        )
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewScanResults() {
    LoadingLayout(title = "Hold on! We are loading stuffs")
}*/