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
Dim zip As ABZipUnzip
Dim sd As String = File.DirRootExternal & "/"
Dim lb As Label
Dim bg As ColorDrawable
Dim mm As Typeface : mm = mm.LoadFromAssets("MyFont.ttf")

	Dim ph As Phone
	Dim Banner As AdView
	Dim Interstitial As InterstitialAd
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.Title = "Oppo"
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
	lb.Text = "1. ပထမဦးစြာ Install ကိုနွိပ္ျပီး " &CRLF& "Myanmar Love(ifont) ကို Install လုပ္လိုက္ပါ။" &CRLF&CRLF& "2. Install လုပ္ျပီးပါက Done ကိုနွိပ္ပါ။" &CRLF& "ၿပီးလၽွင္ေအာက္က Change Font ကိုနွိပ္ပါ။" &CRLF& "Font သို့ Font Style ထဲကိုဆက္ဝင္ျပီး" &CRLF& "Myanmar Love Font(ifont) ကိုေရြးေပးလိုက္ပါ။"
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
	Msgbox("1. Install Font App" & CRLF & "2. After Click Change Font and Choose your Font!","Attention!")
	File.Copy(File.DirAssets,"MyFont.zip",File.DirRootExternal,"MyFont.zip")
	zip.ABUnzip(sd & "MyFont.zip",sd & "MyFont")
	File.Delete(File.DirRootExternal,"MyFont.zip")
	Dim i As Intent
    i.Initialize(i.ACTION_VIEW,"file:///"&File.DirRootExternal& "/MyFont/MyFont.apk")
	i.SetType("application/vnd.android.package-archive")
	StartActivity(i)
End Sub

Sub b2_Click
		If ph.SdkVersion > 19 Then
		ad1.Enabled = True
	End If
			ToastMessageShow("1. Click Font Style Or Font > " &CRLF& "2. Choose Myanmar Love Font(iFont)",True)
	Dim abc As Intent
	abc.Initialize("android.settings.DISPLAY_SETTINGS","")
	StartActivity(abc)
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
