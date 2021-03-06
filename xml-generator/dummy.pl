#ESC/Java2を実行せず、出力ファイル名のみ返すダミースクリプト

$java_filename = $ARGV[0];	#入力javaファイル名
$xml_dir = $ARGV[1];		#出力ディレクトリ

#StockManagement
if ($java_filename =~ ".*AppMain.java") {
	print "$xml_dir\main.AppMain.xml";
}
elsif ($java_filename =~ ".*ContainerItem.java") {
	print "$xml_dir\StockManagement.ContainerItem.xml";
}
elsif ($java_filename =~ ".*Item.java") {
	print "$xml_dir\StockManagement.Item.xml";
}
elsif ($java_filename =~ ".*Customer.java") {
	print "$xml_dir\StockManagement.Customer.xml";
}
elsif ($java_filename =~ ".*ReceiptionDesk.java") {
	print "$xml_dir\StockManagement.ReceiptionDesk.xml";
}
elsif ($java_filename =~ ".*Request.java") {
	print "$xml_dir\StockManagement.Request.xml";
}
elsif ($java_filename =~ ".*StockState.java") {
	print "$xml_dir\StockManagement.StockState.xml";
}
elsif ($java_filename =~ ".*Storage.java") {
	print "$xml_dir\StockManagement.Storage.xml";
}

#SCVTestData
elsif ($java_filename =~ "scvtestdata.main.SCVTestData.java") {
	print "$xml_dir\scvtestdata.main.SCVTestData.xml";
}
elsif ($java_filename =~ "scvtestdata.model.Group.java") {
	print "$xml_dir\scvtestdata.model.Group.xml";
}
elsif ($java_filename =~ "scvtestdata.model.Laboratory.java") {
	print "$xml_dir\scvtestdata.model.Laboratory.xml";
}
elsif ($java_filename =~ "scvtestdata.model.Staff.java") {
	print "$xml_dir\scvtestdata.model.Staff.xml";
}
elsif ($java_filename =~ "scvtestdata.model.Student.java") {
	print "$xml_dir\scvtestdata.model.Student.xml";
}

#AdressBook-teacher-jml
elsif ($java_filename =~ "addressbook.AddressBook.java") {
	print "$xml_dir\\addressbook.AddressBook.xml";
}
elsif ($java_filename =~ "addressbook.Entry.java") {
	print "$xml_dir\\addressbook.Entry.xml";
}
elsif ($java_filename =~ "addressbook.MailDomainComparator.java") {
	print "$xml_dir\\addressbook.MailDomainComparator.xml";
}
elsif ($java_filename =~ "addressbook.NameComparator.java") {
	print "$xml_dir\\addressbook.NameComparator.xml";
}
elsif ($java_filename =~ "addressbook.TelComparator.java") {
	print "$xml_dir\\addressbook.TelComparator.xml";
}


#それ以外
else {
	print "No such input file";
	exit(1);
}

exit(0);
