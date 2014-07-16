/Applications/android-sdk-macosx/platform-tools/adb -d shell 'run-as net.kuwalab.android.icareader cat databases/ica_db > /sdcard/ica_db'
/Applications/android-sdk-macosx/platform-tools/adb pull /sdcard/ica_db ~/temp/ica_db
sqlite3 ~/temp/ica_db
