CREATE OR REPLACE  
    PROCEDURE insert_logger(tabla VARCHAR, idrow VARCHAR, operacion VARCHAR, typeValue VARCHAR, cuit varchar, esquema VARCHAR, rv a_record) 
    as   $$
    DECLARE
     str VARCHAR(32000) := '';
    BEGIN
    		str := 'INSERT INTO LOGGER (idrow,tabla,operacion,typevalue,fecha_operacion,cuit,esquema' ||
    		',a01_n,a01_v' ||
    		',a02_n,a02_v' ||
    		',a03_n,a03_v' ||
    		',a04_n,a04_v' ||
    		',a05_n,a05_v' ||
    		',a06_n,a06_v' ||
    		',a07_n,a07_v' ||
    		',a08_n,a08_v' ||
    		',a09_n,a09_v' ||
    		',a10_n,a10_v' ||
    		',a11_n,a11_v' ||
    		',a12_n,a12_v' ||
    		',a13_n,a13_v' ||
    		',a14_n,a14_v' ||
    		',a15_n,a15_v' ||
    		',a16_n,a16_v' ||
    		',a17_n,a17_v' ||
    		',a18_n,a18_v' ||
    		',a19_n,a19_v' ||
    		',a20_n,a20_v' ||
    		',a21_n,a21_v' ||
    		',a22_n,a22_v' ||
    		',a23_n,a23_v' ||
    		',a24_n,a24_v' ||
    		',a25_n,a25_v' ||
    		',a26_n,a26_v' ||
    		',a27_n,a27_v' ||
    		',a28_n,a28_v' ||
    		',a29_n,a29_v' ||
    		',a30_n,a30_v' ||
    		',a31_n,a31_v' ||
    		',a32_n,a32_v' ||
    		',a33_n,a33_v' ||
    		',a34_n,a34_v' ||
    		',a35_n,a35_v' ||
    		',a36_n,a36_v' ||
    		',a37_n,a37_v' ||
    		',a38_n,a38_v' ||
    		',a39_n,a39_v' ||
    		',a40_n,a40_v' ||
    		',a41_n,a41_v' ||
    		',a42_n,a42_v' ||
    		',a43_n,a43_v' ||
    		',a44_n,a44_v' ||
    		',a45_n,a45_v' ||
    		',a46_n,a46_v' ||
    		',a47_n,a47_v' ||
    		',a48_n,a48_v' ||
    		',a49_n,a49_v' ||
    		',a50_n,a50_v' ||
    		')' ||
    		' values ($1,$2,$3,$4,now(),$5,$6' ||
    		',$7,$8' ||
    		',$9,$10' ||
    		',$11,$12' ||
    		',$13,$14' ||
    		',$15,$16' ||
    		',$17,$18' ||
    		',$19,$20' ||
    		',$21,$22' ||
    		',$23,$24' ||
    		',$25,$26' ||
    		',$27,$28' ||
    		',$29,$30' ||
    		',$31,$32' ||
    		',$33,$34' ||
    		',$35,$36' ||
    		',$37,$38' ||
    		',$39,$40' ||
    		',$41,$42' ||
    		',$43,$44' ||
    		',$45,$46' ||
    		',$47,$48' ||
    		',$49,$50' ||
    		',$51,$52' ||
    		',$53,$54' ||
    		',$55,$56' ||
    		',$57,$58' ||
    		',$59,$60' ||
    		',$61,$62' ||
    		',$63,$64' ||
    		',$65,$66' ||
    		',$67,$68' ||
    		',$69,$70' ||
    		',$71,$72' ||
    		',$73,$74' ||
    		',$75,$76' ||
    		',$77,$78' ||
    		',$79,$80' ||
    		',$81,$82' ||
    		',$83,$84' ||
    		',$85,$86' ||
    		',$87,$88' ||
    		',$89,$90' ||
    		',$91,$92' ||
    		',$93,$94' ||
    		',$95,$96' ||
    		',$97,$98' ||
    		',$99,$100' ||
    		',$101,$102' ||
    		',$103,$104' ||
    		',$105,$106' ||
    		')';
          
          execute str USING idrow,tabla,operacion,typeValue,cuit,esquema
    		   ,rv.a01_n,rv.a01_v
    		   ,rv.a02_n,rv.a02_v
    		   ,rv.a03_n,rv.a03_v
    		   ,rv.a04_n,rv.a04_v
    		   ,rv.a05_n,rv.a05_v
    		   ,rv.a06_n,rv.a06_v
    		   ,rv.a07_n,rv.a07_v
    		   ,rv.a08_n,rv.a08_v
    		   ,rv.a09_n,rv.a09_v
    		   ,rv.a10_n,rv.a10_v
    		   ,rv.a11_n,rv.a11_v
    		   ,rv.a12_n,rv.a12_v
    		   ,rv.a13_n,rv.a13_v
    		   ,rv.a14_n,rv.a14_v
    		   ,rv.a15_n,rv.a15_v
    		   ,rv.a16_n,rv.a16_v
    		   ,rv.a17_n,rv.a17_v
    		   ,rv.a18_n,rv.a18_v
    		   ,rv.a19_n,rv.a19_v
    		   ,rv.a20_n,rv.a20_v
    		   ,rv.a21_n,rv.a21_v
    		   ,rv.a22_n,rv.a22_v
    		   ,rv.a23_n,rv.a23_v
    		   ,rv.a24_n,rv.a24_v
    		   ,rv.a25_n,rv.a25_v
    		   ,rv.a26_n,rv.a26_v
    		   ,rv.a27_n,rv.a27_v
    		   ,rv.a28_n,rv.a28_v
    		   ,rv.a29_n,rv.a29_v
    		   ,rv.a30_n,rv.a30_v
    		   ,rv.a31_n,rv.a31_v
    		   ,rv.a32_n,rv.a32_v
    		   ,rv.a33_n,rv.a33_v
    		   ,rv.a34_n,rv.a34_v
    		   ,rv.a35_n,rv.a35_v
    		   ,rv.a36_n,rv.a36_v
    		   ,rv.a37_n,rv.a37_v
    		   ,rv.a38_n,rv.a38_v
    		   ,rv.a39_n,rv.a39_v
    		   ,rv.a40_n,rv.a40_v
    		   ,rv.a41_n,rv.a41_v
    		   ,rv.a42_n,rv.a42_v
    		   ,rv.a43_n,rv.a43_v
    		   ,rv.a44_n,rv.a44_v
    		   ,rv.a45_n,rv.a45_v
    		   ,rv.a46_n,rv.a46_v
    		   ,rv.a47_n,rv.a47_v
    		   ,rv.a48_n,rv.a48_v
    		   ,rv.a49_n,rv.a49_v
    		   ,rv.a50_n,rv.a50_v;
    END;
    $$ LANGUAGE PLPGSQL;
