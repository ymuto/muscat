#ESC/Java2�����s�����A�o�̓t�@�C�����̂ݕԂ��_�~�[�X�N���v�g

$java_filename = $ARGV[0];	#����java�t�@�C����
$xml_dir = $ARGV[1];		#�o�̓f�B���N�g��

if ($java_filename =~ ".*AppMain.java") {
	print "$xml_dir\main.AppMain.xml";
}
elsif ($java_filename =~ ".*ContainerItem.java") {
	print "$xml_dir\StockManagement.ContainerItem.xml";
}
elsif ($java_filename =~ ".*Customer.java") {
	print "$xml_dir\StockManagement.Customer.xml";
}
elsif ($java_filename =~ ".*Item.java") {
	print "$xml_dir\StockManagement.Item.xml";
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
else {
	print "error";
	exit(1);
}

exit(0);
