Type=Activity
Version=6.5
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
Dim ad1,ad2 As Timer
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
Dim b1,b2 As Button
Dim lb As Label
Dim bg As ColorDrawable
Dim mm As Typeface : mm = mm.LoadFromAssets("MyFont.ttf")

	Dim ph As Phone
	Dim Banner As AdView
	Dim Interstitial As InterstitialAd
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.Title = "Huawei"
	Banner.Initialize("Banner","ca-app-pub-4173348573252986/7724177755")
	Banner.LoadAd
	Activity.AddView(Banner,0%x,100%y - 50dip,100%x,50dip)
		
	Interstitial.Initialize("Interstitial","ca-app-pub-4173348573252986/9200910957")
	Interstitial.LoadAd
		
		ad1.Initialize("ad1",100)
		ad1.Enabled = False
		ad2.Initialize("ad2",30000)
		ad2.Enabled = True

	Activity.Color = Colors.White
	ph.SetScreenOrientation(1)
	
	lb.Initialize("lb")
	lb.Text = "1. ပထမဦးစြာ Install ကိုနွိပ္" &CRLF& "ျပီးရင္ Change Font ကိုနွိပ္ပါ။" &CRLF&CRLF& "Me ကိုဆက္ဝင္ျပီး Myanmar Love Font ကိုရွာပါ။" &CRLF& "ေတြ့ရင္ နွိပ္ျပီး Apply လုပ္ေပးလိုက္ပါ။" &CRLF& "အဆင္မေျပရင္ဖုန္းကို Restart ခ်ေပးျကည့္ပါ။"
	Activity.AddView(lb,2%x,2%y,90%x,35%y)
	lb.TextColor = Colors.Black
	lb.Typeface = mm
	
	bg.Initialize(Colors.Black,15)
b1.Initialize("b1")
b1.Text = "Install"
b1.Background = bg

b2.Initialize("b2")
b2.Text = "Change Font"
b2.Background = bg

Activity.AddView(b1,20%x,(lb.Top+lb.Height),60%x,50dip)
Activity.AddView(b2,20%x,(b1.Top+b1.Height)+2%y,60%x,50dip)
End Sub

Sub b1_Click
		If ph.SdkVersion > 19 Then
		ad1.Enabled = True
	End If
	File.Copy(File.DirAssets,"MyFont.hwt",File.DirRootExternal & "/HWThemes","MyFont.hwt")
	If File.Exists(File.DirRootExternal & "/HWThemes","MyFont.hwt") Then
		Msgbox("Installed Successfully!","Completed")
		Else
			Msgbox("You phone not Huawei EMUI!","ERROR")
	End If
End Sub

Sub b2_Click
		If ph.SdkVersion > 19 Then
		ad1.Enabled = True
	End If
				ToastMessageShow("1. Click 'Me'> " &CRLF& "2. Choose Myanmar Love Font and" &CRLF& " Apply it",True)
Dim i As Intent
Dim pm As PackageManager
i= pm.GetApplicationIntent("com.huawei.android.thememanager") 
StartActivity(i)
End Sub

Sub ad1_Tick
	If Interstitial.Ready Then Interstitial.Show Else Interstitial.LoadAd
	ad1.Enabled = False
End Sub

Sub ad2_Tick
	If Interstitial.Ready Then Interstitial.Show Else Interstitial.LoadAd
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub