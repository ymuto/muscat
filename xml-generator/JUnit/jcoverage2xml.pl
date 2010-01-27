#JCoverageでエクスポートしたレポート(htmlファイル)からXMLを生成する

use File::Basename;
use File::Spec;
use File::Path;

%ERROR_MESSAGE = (
	'NO_FILE' => 'Input file dont exist.',
	'FORMAT' => 'Unsupported HTML format.'
);

$html_filename = $ARGV[0];	#入力htmlファイル名
$xml_dir = $ARGV[1];		#出力ディレクトリ

$html_filename = 'C:\Users\y-mutoh\research\StockManagement-djUnit-export\index.html';
$xml_dir = 'C:\Users\y-mutoh\research\StockManagement-djUnit-export\xml';


#htmlファイルの内容を変数$htmlに格納
open(IN, $html_filename);
local $/ = undef;
my $html = <IN>; #改行を含む文字列全体
close(IN);

#<h2>All Java&trade; files</h2>のところまで進める
if (!($html =~ m/<h2>All Java\&trade; files<\/h2>/)) {
	print $ERROR_MESSAGE{'FORMAT'};
	exit(1);
}


#クラスの完全限定名取得
while ($html =~ m/<td class="filename">\n<a href=\S*>(\S*)<\/a><\/td>/g) { 
	$full_qualified_name = $1;
	
	#line coverage取得
	$html =~ m/<td class="coverage">\n<a href=$full_qualified_name\.html>(\d*)%<\/a><\/td>/;
	$line_coverage = $1;

	#完全限定名からパッケージ名と単純名を得る
	my $package_name;	
	my $simple_name;
	fullQualifiedName2packageNameAndSimpleName($full_qualified_name, \$package_name, \$simple_name);

	#クラスデータ生成
	my %class_data =  (
		'full_qualified_name' => $full_qualified_name,
		'package_name' => $package_name,
		'simple_name' => $simple_name,
		'line_coverage' => $line_coverage
	);
	
	#XMLファイル出力
	&OutputXml($xml_dir, \%class_data);
}

#終了
exit(0);


#--------------------------------------------
# XMLファイルを出力する
#--------------------------------------------
#引数:
# string 出力XMLディレクトリ
# ハッシュのアドレス クラスデータ
#--------------------------------------------
sub OutputXml {
	my ($xml_dir, $class_data) = @_;
	my ($full_qualified_name, $package_name, $simple_name, $line_coverage) 
		= @$class_data{'full_qualified_name', 'package_name', 'simple_name', 'line_coverage'};
	
	# XMLデータを生成
	my $xml_data =<<EndOfXML;
<?xml version="1.0" encoding="utf-8"?>
<class>
	<packageName>$package_name</packageName>
	<simpleName>$simple_name</simpleName>
	<coverage>$line_coverage</coverage>
</class>
EndOfXML
	
	
	#xmlファイルを出力
	if ($SYNC_PACKAGE_DIRECTORY == 1) { #出力XMLファイル 「パッケージ\クラス名.xml」
		$xml_dirpath = File::Spec->catfile($xml_dir, split(/\./, $package_name));
		makeDirectory($xml_dirpath);
		$xml_filepath = File::Spec->catfile($xml_dirpath, "$simple_name.xml");
	}
	else {	#出力XMLファイル 「パッケージ.クラス名.xml」
		makeDirectory($xml_dir);
		$xml_filepath = File::Spec->catfile($xml_dir, "$full_qualified_name.xml");
	}
	open (OUT, ">$xml_filepath");
	print OUT $xml_data;
	close(OUT);
	#出力ファイル名を出力
	print "$xml_filepath\n";
}

#--------------------------------------------
# 完全限定名を パッケージ名と単純名に分離する
#--------------------------------------------
#引数:
# string 完全限定名
# stringのアドレス パッケージ名へのポインタ
# stringのアドレス 単純名へのポインタ
#--------------------------------------------
sub fullQualifiedName2packageNameAndSimpleName {
	my ($full_qualified_name, $package_name, $simple_name) = @_;
	@elements = split(/\./,$full_qualified_name);
	$last_index = $#elements;
	#単純名
	$$simple_name = pop(@elements);
	#パッケージ名
	$$package_name = join('.', @elements);
}

#以下escj2xml.plからコピペ

#--------------------------------------------
# ディレクトリが存在しなければ作成する
#--------------------------------------------
#引数:
# string ディレクトリパス
#--------------------------------------------
sub makeDirectory {
	my ($dir_path) = @_;
	
	if (!-d $dir_path){
		mkpath $dir_path;
	}
}

#--------------------------------------------
# デバッグ時のみコメントを出力する
#--------------------------------------------
#引数:
# string 出力したいコメント
#--------------------------------------------
sub debugprint {
	if ($DEBUG == 1) {
		printcomment(@_);
	}
}

#--------------------------------------------
# コメントを出力する
#--------------------------------------------
#引数:
# string 出力したいコメント
#--------------------------------------------
sub printcomment {
	my ($arg) = @_;
	@lines = split(/\n/, $arg);
	foreach $line (@lines) {
		print "#$line\n";
	}
}
