package vdsMain.message;

import androidx.annotation.NonNull;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.message.TokenParser;
import org.spongycastle.pqc.math.linearalgebra.Matrix;
import vdsMain.CreateGroupMessage;
import vdsMain.wallet.Wallet;

//bmo
public class VMessageFactory extends MessageFactory {
    public VMessageFactory(@NonNull Wallet izVar) {
        super(izVar);
    }

    //mo41725a
    public Message getMessageByCommand(String command, Wallet wallet) {
        char c;
        switch (command.hashCode()) {
            case -1958018905:
                if (command.equals("createotcack")) {
                    c = 'T';
                    break;
                }
            case -1927545674:
                if (command.equals("serviceport")) {
                    c = 'k';
                    break;
                }
            case -1794889101:
                if (command.equals("setcurrency")) {
                    c = 'O';
                    break;
                }
            case -1787928698:
                if (command.equals("gettopcllist")) {
                    c = '$';
                    break;
                }
            case -1700792280:
                if (command.equals("setgruleack")) {
                    c = 'F';
                    break;
                }
            case -1685376880:
                if (command.equals("otcstatus")) {
                    c = '_';
                    break;
                }
            case -1552346018:
                if (command.equals("filterload")) {
                    c = 17;
                    break;
                }
            case -1536231419:
                if (command.equals("isgpaddrack")) {
                    c = 'd';
                    break;
                }
            case -1527499207:
                if (command.equals("setgnameack")) {
                    c = 'B';
                    break;
                }
            case -1486167549:
                if (command.equals("incmaxack")) {
                    c = ':';
                    break;
                }
            case -1437185393:
                if (command.equals("crtgroupack")) {
                    c = '0';
                    break;
                }
            case -1426264490:
                if (command.equals("setgfotcpack")) {
                    c = Matrix.MATRIX_TYPE_RANDOM_LT;
                    break;
                }
            case -1385269615:
                if (command.equals("iqconactack")) {
                    c = 'j';
                    break;
                }
            case -1367724422:
                if (command.equals("cancel")) {
                    c = ']';
                    break;
                }
            case -1249318055:
                if (command.equals("gettxs")) {
                    c = '!';
                    break;
                }
            case -1249316615:
                if (command.equals("getvib")) {
                    c = 25;
                    break;
                }
            case -1184261946:
                if (command.equals("incmax")) {
                    c = '9';
                    break;
                }
            case -1176315169:
                if (command.equals("getvibquene")) {
                    c = 27;
                    break;
                }
            case -1176314952:
                if (command.equals("getvibqueue")) {
                    c = 28;
                    break;
                }
            case -1135176588:
                if (command.equals("rmvmemberack")) {
                    c = '8';
                    break;
                }
            case -1052776876:
                if (command.equals("getluckynode")) {
                    c = '(';
                    break;
                }
            case -995214882:
                if (command.equals("payfee")) {
                    c = 'W';
                    break;
                }
            case -962555060:
                if (command.equals("topclist")) {
                    c = '%';
                    break;
                }
            case -934710369:
                if (command.equals("reject")) {
                    c = 7;
                    break;
                }
            case -894845447:
                if (command.equals("otcstatusack")) {
                    c = '`';
                    break;
                }
            case -886483499:
                if (command.equals("filterclear")) {
                    c = 19;
                    break;
                }
            case -881370583:
                if (command.equals("filteradd")) {
                    c = 18;
                    break;
                }
            case -821876135:
                if (command.equals("vcount")) {
                    c = '+';
                    break;
                }
            case -819959290:
                if (command.equals("verack")) {
                    c = 9;
                    break;
                }
            case -799646674:
                if (command.equals("addmemberack")) {
                    c = '4';
                    break;
                }
            case -795218820:
                if (command.equals("refmemberack")) {
                    c = '6';
                    break;
                }
            case -793050291:
                if (command.equals("approve")) {
                    c = 'Y';
                    break;
                }
            case -707901203:
                if (command.equals("refmember")) {
                    c = '5';
                    break;
                }
            case -666649080:
                if (command.equals("precreatecl")) {
                    c = TokenParser.DQUOTE;
                    break;
                }
            case -638028959:
                if (command.equals("clurtoprec")) {
                    c = 'i';
                    break;
                }
            case -520806894:
                if (command.equals("judgeack")) {
                    c = '\\';
                    break;
                }
            case -486239485:
                if (command.equals("creategroup")) {
                    c = IOUtils.DIR_SEPARATOR_UNIX;
                    break;
                }
            case -287208981:
                if (command.equals("payfeeack")) {
                    c = 'X';
                    break;
                }
            case -280098615:
                if (command.equals("luckynodes")) {
                    c = ')';
                    break;
                }
            case -243311355:
                if (command.equals("psncancel")) {
                    c = '=';
                    break;
                }
            case -176986006:
                if (command.equals("getvibaddr")) {
                    c = 26;
                    break;
                }
            case -143868130:
                if (command.equals("psnexitg")) {
                    c = '?';
                    break;
                }
            case -74739641:
                if (command.equals("getaddr")) {
                    c = 15;
                    break;
                }
            case -74671944:
                if (command.equals("getclrk")) {
                    c = '&';
                    break;
                }
            case -74652672:
                if (command.equals("getdata")) {
                    c = TokenParser.CR;
                    break;
                }
            case 3107:
                if (command.equals("ad")) {
                    c = '-';
                    break;
                }
            case 3177:
                if (command.equals("cl")) {
                    c = 'a';
                    break;
                }
            case 3716:
                if (command.equals("tx")) {
                    c = 11;
                    break;
                }
            case 104433:
                if (command.equals("inv")) {
                    c = 10;
                    break;
                }
            case 114231:
                if (command.equals("stx")) {
                    c = 22;
                    break;
                }
            case 2989041:
                if (command.equals("addr")) {
                    c = 16;
                    break;
                }
            case 3056738:
                if (command.equals("clrk")) {
                    c = '\'';
                    break;
                }
            case 3169250:
                if (command.equals("getx")) {
                    c = 21;
                    break;
                }
            case 3441010:
                if (command.equals("ping")) {
                    c = 5;
                    break;
                }
            case 3446776:
                if (command.equals("pong")) {
                    c = 6;
                    break;
                }
            case 3541211:
                if (command.equals("stx2")) {
                    c = 24;
                    break;
                }
            case 92660368:
                if (command.equals("addra")) {
                    c = 'f';
                    break;
                }
            case 92660371:
                if (command.equals("addrd")) {
                    c = 'g';
                    break;
                }
            case 92660388:
                if (command.equals("addru")) {
                    c = 'e';
                    break;
                }
            case 92899676:
                if (command.equals("alert")) {
                    c = 0;
                    break;
                }
            case 93732676:
                if (command.equals("bidls")) {
                    c = FilenameUtils.EXTENSION_SEPARATOR;
                    break;
                }
            case 93832333:
                if (command.equals("block")) {
                    c = 1;
                    break;
                }
            case 98246137:
                if (command.equals("getad")) {
                    c = ',';
                    break;
                }
            case 98246207:
                if (command.equals("getcl")) {
                    c = 'b';
                    break;
                }
            case 98246800:
                if (command.equals("getx2")) {
                    c = 23;
                    break;
                }
            case 101478167:
                if (command.equals("judge")) {
                    c = '[';
                    break;
                }
            case 106432991:
                if (command.equals("pandf")) {
                    c = Matrix.MATRIX_TYPE_RANDOM_UT;
                    break;
                }
            case 158161602:
                if (command.equals("setotccack")) {
                    c = 'N';
                    break;
                }
            case 158548885:
                if (command.equals("setotcpack")) {
                    c = 'H';
                    break;
                }
            case 167514992:
                if (command.equals("getheaders")) {
                    c = 3;
                    break;
                }
            case 203806049:
                if (command.equals("setbaseinfo")) {
                    c = '1';
                    break;
                }
            case 306928827:
                if (command.equals("gcluetoprec")) {
                    c = 'h';
                    break;
                }
            case 350425604:
                if (command.equals("isgpaddr")) {
                    c = 'c';
                    break;
                }
            case 351608024:
                if (command.equals("version")) {
                    c = 8;
                    break;
                }
            case 396689792:
                if (command.equals("gvcount")) {
                    c = '*';
                    break;
                }
            case 401996971:
                if (command.equals("psnexitgack")) {
                    c = '@';
                    break;
                }
            case 450806912:
                if (command.equals("vibaddr")) {
                    c = 30;
                    break;
                }
            case 451054909:
                if (command.equals("vibinfo")) {
                    c = 29;
                    break;
                }
            case 476577743:
                if (command.equals("cancelack")) {
                    c = '^';
                    break;
                }
            case 598383778:
                if (command.equals("createotc")) {
                    c = 'S';
                    break;
                }
            case 606599130:
                if (command.equals("stbinfoack")) {
                    c = '2';
                    break;
                }
            case 731203195:
                if (command.equals("addmember")) {
                    c = '3';
                    break;
                }
            case 795307910:
                if (command.equals("headers")) {
                    c = 4;
                    break;
                }
            case 853972508:
                if (command.equals("approveack")) {
                    c = Matrix.MATRIX_TYPE_ZERO;
                    break;
                }
            case 949308273:
                if (command.equals("mempool")) {
                    c = 12;
                    break;
                }
            case 990062531:
                if (command.equals("merkleblock")) {
                    c = 20;
                    break;
                }
            case 1059466826:
                if (command.equals("pandfack")) {
                    c = 'V';
                    break;
                }
            case 1105396105:
                if (command.equals("vibquene")) {
                    c = 31;
                    break;
                }
            case 1105396322:
                if (command.equals("vibqueue")) {
                    c = TokenParser.SP;
                    break;
                }
            case 1114599309:
                if (command.equals("setgannack")) {
                    c = 'D';
                    break;
                }
            case 1148986707:
                if (command.equals("setgfotcp")) {
                    c = 'K';
                    break;
                }
            case 1225983068:
                if (command.equals("getblocks")) {
                    c = 2;
                    break;
                }
            case 1249165256:
                if (command.equals("setgfeeack")) {
                    c = 'J';
                    break;
                }
            case 1295661960:
                if (command.equals("psnaddgrack")) {
                    c = '<';
                    break;
                }
            case 1295675977:
                if (command.equals("psnaddgroup")) {
                    c = ';';
                    break;
                }
            case 1373529878:
                if (command.equals("clprecreate")) {
                    c = '#';
                    break;
                }
            case 1390573453:
                if (command.equals("setotcack")) {
                    c = Matrix.MATRIX_TYPE_RANDOM_REGULAR;
                    break;
                }
            case 1416315236:
                if (command.equals("psncancelack")) {
                    c = '>';
                    break;
                }
            case 1422762064:
                if (command.equals("setgname")) {
                    c = 'A';
                    break;
                }
            case 1422900417:
                if (command.equals("setgrule")) {
                    c = 'E';
                    break;
                }
            case 1582872719:
                if (command.equals("notfound")) {
                    c = 14;
                    break;
                }
            case 1664836597:
                if (command.equals("rmvmember")) {
                    c = '7';
                    break;
                }
            case 1985426600:
                if (command.equals("setcack")) {
                    c = 'P';
                    break;
                }
            case 1985546108:
                if (command.equals("setgann")) {
                    c = 'C';
                    break;
                }
            case 1985550625:
                if (command.equals("setgfee")) {
                    c = 'I';
                    break;
                }
            case 1985802343:
                if (command.equals("setotcc")) {
                    c = 'M';
                    break;
                }
            case 1985802356:
                if (command.equals("setotcp")) {
                    c = 'G';
                    break;
                }
            case 1985917229:
                if (command.equals("setsotc")) {
                    c = 'Q';
                    break;
                }
            case -1479134629:
                if (command.equals("getanonytx")) {
                    c = 'l';
                    break;
                }
            case -851341711:
                if (command.equals("anonytx")) {
                    c = 'm';
                    break;
                }
            case 1215945707:
                if (command.equals("getbasetx")) {
                    c = 'n';
                    break;
                }
            case -1396202219:
                if (command.equals("basetx")) {
                    c = 'o';
                    break;
                }
            case -1775470392:
                if (command.equals("getaddrtxs")) {
                    c = 'p';
                    break;
                }
            case -1147677474:
                if (command.equals("addrtxs")) {
                    c = 'q';
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return new AlertMessage(wallet);
            case 1:
                return new BlockMessage(wallet);
            case 2:
                return new GetBlocksMessage(wallet);
            case 3:
                return new GetHeadersMessage(wallet);
            case 4:
                return new HeadersMessage(wallet);
            case 5:
                return new PingMessage(wallet);
            case 6:
                return new PongMessage(wallet);
            case 7:
                return new RejectMessage(wallet);
            case 8:
                return new VersionMessage(wallet);
            case 9:
                return new VerackMessage(wallet);
            case 10:
                return new InvMessage(wallet);
            case 11:
                return new TxMessage(wallet);
            case 12:
                return new MempoolMessage(wallet);
            case 13:
                return new GetDataMessage(wallet);
            case 14:
                return new NotFoundMessage(wallet);
            case 15:
                return new GetAddrMessage(wallet);
            case 16:
                return new AddrMessage(wallet);
            case 17:
                return new FilterLoadMessage(wallet);
            case 18:
                return new FilterAddMessage(wallet);
            case 19:
                return new FilterClearMessage(wallet);
            case 20:
                return new MerkelBlockMessage(wallet);
            case 21:
                return new GetTxMessage(wallet);
            case 22:
                return new STXMessage(wallet);
            case 23:
                return new GetTx2Message(wallet);
            case 24:
                return new STX2Message(wallet);
            case 25:
                return new GetVibMessage(wallet);
            case 26:
                return new GetVibAddressMessage(wallet);
            case 27:
                return new GetVibWaitQueneMessage(wallet);
            case 28:
                return new GetVibWaitQueue2Message(wallet);
            case 29:
                return new VibMessage(wallet);
            case 30:
                return new VibAddressMessage(wallet);
            case 31:
                return new VibWaitQueneMessage(wallet);
            case ' ':
                return new VibWaitQueue2Message(wallet);
            case '!':
                return new GetTxsMessage(wallet);
            case '\"':
                return new PreCreateMessage(wallet);
            case '#':
                return new CluePreCreateTreeMssage(wallet);
            case '$':
                return new GetLastSeasonTopClueListMessage(wallet);
            case '%':
                return new LastSeasonTopClueListMessage(wallet);
            case '&':
                return new GetLastWeekClueRankingMessage(wallet);
            case '\'':
                return new LastWeekClueRankingMessage(this.wallet);
            case '(':
                return new GetLuckyNodesMessage(this.wallet);
            case ')':
                return new LuckNodesMessage(this.wallet);
            case '*':
                return new GetVCountMessage(this.wallet);
            case '+':
                return new VCountMessage(this.wallet);
            case ',':
                return new GetAdMessage(this.wallet);
            case '-':
                return new AdListMessage(this.wallet);
            case '.':
                return new BidListMessage(this.wallet);
            case '/':
                return new CreateGroupMessage(this.wallet, 0);
            case '0':
                return new CreateGroupAckMessage(this.wallet, 0);
            case '1':
                return new SetGroupBaseInfoMessage(this.wallet, 0);
            case '2':
                return new SetGroupBaseInfoAckMessage(this.wallet, 0);
            case '3':
                return new AddGroupMemberMessage(this.wallet, 0);
            case '4':
                return new AddGroupMemberAckMessage(this.wallet, 0);
            case '5':
                return new RefuseGroupMemberMessage(this.wallet, 0);
            case '6':
                return new RefuseGroupMemberAckMessage(this.wallet, 0);
            case '7':
                return new RemoveGroupMemberMessage(this.wallet, 0);
            case '8':
                return new RemoveGroupMemberAckMessage(this.wallet, 0);
            case '9':
                return new IncreaseGroupMaxMemberMessage(this.wallet, 0);
            case ':':
                return new IncreaseGroupMaxMemberAckMessage(this.wallet, 0);
            case ';':
                return new JoinGroupMessage(this.wallet, 0);
            case '<':
                return new JoinGroupAckMessage(this.wallet, 0);
            case '=':
                return new CancelJoinGroupMessage(this.wallet, 0);
            case '>':
                return new CacncleJoinGroupAckMessage(this.wallet, 0);
            case '?':
                return new ExitGroupMessage(this.wallet, 0);
            case '@':
                return new ExitGroupAckMessage(this.wallet, 0);
            case 'A':
                return new SetGroupNameMessage(this.wallet, 0);
            case 'B':
                return new SetGroupNameAckMessage(this.wallet, 0);
            case 'C':
                return new SetGroupAnnounceMessage(this.wallet, 0);
            case 'D':
                return new SetGroupAnnounceAckMessage(this.wallet, 0);
            case 'E':
                return new SetGroupRuleMessage(this.wallet, 0);
            case 'F':
                return new SetGroupRuleAckMessage(this.wallet, 0);
            case 'G':
                return new SetOtcPercentMessage(this.wallet, 0);
            case 'H':
                return new SetOtcPercentAckMessage(this.wallet, 0);
            case 'I':
                return new SetGroupFeeMessage(this.wallet, 0);
            case 'J':
                return new SetGroupFeeAckMessage(this.wallet, 0);
            case 'K':
                return new SetGroupFeeOtcPercentMessage(this.wallet, 0);
            case 'L':
                return new SetGroupFeeOtcPercentAckMessage(this.wallet, 0);
            case 'M':
                return new SetOtcPercentCurrencyMessage(this.wallet, 0);
            case 'N':
                return new SetOtcPercentCurrencyAckMessage(this.wallet, 0);
            case 'O':
                return new SetCurrencyMessage(this.wallet, 0);
            case 'P':
                return new SetCurrencyAckMessage(this.wallet, 0);
            case 'Q':
                return new SetStartOtcMessage(this.wallet, 0);
            case 'R':
                return new SetStartOtcAckMessage(this.wallet, 0);
            case 'S':
                return new CreateOtcMessage(this.wallet, 0);
            case 'T':
                return new CreateOtcAckMessage(this.wallet, 0);
            case 'U':
                return new PayVulumnAndFeeMessage(this.wallet, 0);
            case 'V':
                return new PayVulumnAndFeeAckMessage(this.wallet, 0);
            case 'W':
                return new PayFeeMessage(this.wallet, 0);
            case 'X':
                return new PayFeeAckMessage(this.wallet, 0);
            case 'Y':
                return new ApproveMessage(this.wallet, 0);
            case 'Z':
                return new ApproveAckMessage(this.wallet, 0);
            case '[':
                return new JudgeMessage(this.wallet, 0);
            case '\\':
                return new JudgeAckMessage(this.wallet, 0);
            case ']':
                return new CancelDealMessage(this.wallet, 0);
            case '^':
                return new CancelDealAckMessage(this.wallet, 0);
            case '_':
                return new OtcStatusMessage(this.wallet, 0);
            case '`':
                return new OtcStatusAckMessage(this.wallet, 0);
            case 'a':
                return new ClueMessage(this.wallet);
            case 'b':
                return new GetClueMessage(this.wallet);
            case 'c':
                return new IsGroupAddrMessage(this.wallet);
            case 'd':
                return new IsGroupAddrAckMessage(this.wallet);
            case 'e':
                return new AddrUpdateMessage(this.wallet);
            case 'f':
                return new AddrAddMessage(this.wallet);
            case 'g':
                return new AddrDeleteMessage(this.wallet);
            case 'h':
                return new GetClueTopRecordMessage(this.wallet);
            case 'i':
                return new ClueTopListRecordMessage(this.wallet);
            case 'j':
                return new ContractTxResultAckMessage(this.wallet);
            case 'k':
                return new ServicePortMessage(this.wallet);
            case 'l':
                return new GetAnonTxMessage(this.wallet);
            case 'm':
                return new AnonTxMessage(this.wallet);
            case 'n':
                return new GetBaseTxMessage(this.wallet);
            case 'o':
                return new BaseTxMessage(this.wallet);
            case 'p':
                return new GetAddrTxsMessage(this.wallet);
            case 'q':
                return new AddrTxsMessage(this.wallet);
            default:
                return null;
        }
    }

    //mo41724a
    public Message getUnKonwnMessage(String str) {
        return new UnknownMessage(this.wallet, str);
    }
}

