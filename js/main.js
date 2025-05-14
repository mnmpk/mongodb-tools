
$(window).on('load', function () {
    append = function () {
        let cId = this.parentElement.parentElement.id;
        /*if ("awsEnvVar" == this.id)
            cId = "awsEnvVar";*/
        if ($("#final_" + cId).length) {
            $("#final_" + cId).remove();
        } else {
            let value = $("#" + cId + " code").text();
            if ("awsSetting" == cId) {
                value = $('#awsEnvVar').val();
                //value.replaceAll("\n","<br/>");
            }
            let container = "final";
            switch (cId) {
                case "awsSetting":
                case "installJq":
                case "installMongoCli":
                case "installMongosh":
                case "installAwsCli":
                case "removeServers":
                case "securityGroup":
                case "placementGroup":
                case "launchInstance":
                case "dns":
                case "hosts":
                    container = "local";
                    break;
            }
            $('#' + container).append("<pre id='final_" + cId + "'>" + value + "</pre>");
        }
    }
    $('input[type=checkbox]').on('click', append);

    refreshAws = () => {
        $('#final_awsSetting').text($('#awsEnvVar').val());
    }
    awsSetting = function () {
        newLines = "";
        newLines += "export AWS_DEFAULT_REGION=" + $('#awsRegion').val() + "\n";
        newLines += "export AWS_ACCESS_KEY_ID=\"" + $('#awsAccessKeyId').val() + "\"\n";
        newLines += "export AWS_SECRET_ACCESS_KEY=\"" + $('#awsSecretAccessKey').val() + "\"\n";
        newLines += "export AWS_SESSION_TOKEN=\"" + $('#awsSessionToken').val() + "\"";
        $("#awsEnvVar").val(newLines);
        refreshAws();
    }
    $('#awsSetting .form-control').on('change', awsSetting);

    $('#awsEnvVar').on('change', refreshAws);

    launchInstances = function () {
        newLines = "\n";
        newLines += "export REGION=\"" + $('#awsRegion').val() + "\"\n";
        if ($('#securityGroup input[type=checkbox]').is(':checked')) {
            newLines += "export SG_NAME=\"" + $('#awsName').val() + " SG\"\n";
        } else {
            newLines += "export SG_NAME=\"allowAll\"\n";
        }
        if ($('#placementGroup input[type=checkbox]').is(':checked')) {
            newLines += "export PG_NAME=\"" + $('#awsName').val() + " PG\"\n";
        } else {
            newLines += "export PG_NAME=\"cluster\"\n";
        }
        newLines += "export ITYPE=\"" + $('#launchInstanceInstanceType').val() + "\"\n";
        newLines += "export IMG_NAME=\"" + $('#launchInstanceImageName').val() + "\"\n";
        newLines += "export DISKS='" + $('#launchInstanceDisks').val() + "'\n";
        newLines += "export KEY_NAME=\"" + $('#launchInstanceKeyName').val() + "\"\n";
        newLines += "export TAGS='ResourceType=instance,Tags=[{Key=Name, Value=\"" + $('#awsName').val() + "\"}, {Key=owner, Value=\"" + $('#awsOwner').val() + "\"}, {Key=\"expire-on\", Value=\"" + $("#awsExpireOn").val() + "\"}]'\n";
        newLines += "export NO_OF_HOST=" + $('#launchInstanceNoOfHosts').val();
        $("#launchInstance code").text(replaceFirstNLines($("#launchInstance code").text(), newLines));
    }
    $('#launchInstance .form-control').on('change', launchInstances);


    dns = function () {
        newLines = "\n";
        newLines += "export OWNER=\"" + $('#awsOwner').val() + "\"\n";
        newLines += "export TAGNAME=\"" + $('#awsName').val() + "\"\n";
        newLines += "export DOMAIN=\"" + "mdbrecruit.net" + "\"\n";
        newLines += "export HOSTEDZONE=\"" + $('#dnsHostedZoneId').val() + "\"\n";
        newLines += "export INTERNAL_SUBDOMAIN=\"" + $('#intSubdomain').val() + "\"\n";
        newLines += "export EXTERNAL_SUBDOMAIN=\"" + $('#extSubdomain').val() + "\"";
        $("#dns code").text(replaceFirstNLines($("#dns code").text(), newLines));
    }
    $('#dns .form-control').on('change', dns);

    hosts = function () {
        newLines = "\n";
        newLines += "export DOMAIN=\"" + $('#domainName').val() + "\"\n";
        newLines += "export INTERNAL_SUBDOMAIN=\"" + $('#intSubdomain').val() + "\"\n";
        newLines += "export EXTERNAL_SUBDOMAIN=\"" + $('#extSubdomain').val() + "\"";
        $("#hosts code").text(replaceFirstNLines($("#hosts code").text(), newLines));
    }

    awsTags = function () {
        newLines = "\n";
        newLines += "export REGIONS=(" + $('#awsRegion').val() + ")\n";
        newLines += "export OWNER=\"" + $('#awsOwner').val() + "\"\n";
        newLines += "export TAGNAME=\"" + $('#awsName').val() + "\"";
        $("#removeServers code").text(replaceFirstNLines($("#removeServers code").text(), newLines));

        newLines = "\n";
        newLines += "export REGION=\"" + $('#awsRegion').val() + "\"\n";
        if ($('#securityGroup input[type=checkbox]').is(':checked')) {
            newLines += "export SG_NAME=\"" + $('#awsName').val() + " SG\"\n";
        } else {
            newLines += "export SG_NAME=\"allowAll\"\n";
        }
        newLines += "export SG_DESCRIPTION=\"" + $('#awsName').val() + " SG\"\n";
        $("#securityGroup code").text(replaceFirstNLines($("#securityGroup code").text(), newLines));

        newLines = "\n";
        if ($('#placementGroup input[type=checkbox]').is(':checked')) {
            newLines += "export PG_NAME=\"" + $('#awsName').val() + " PG\"\n";
        } else {
            newLines += "export PG_NAME=\"cluster\"\n";
        }
        $("#placementGroup code").text(replaceFirstNLines($("#placementGroup code").text(), newLines));

        launchInstances();
        dns();
        hosts();
    }
    $('#awsTags .form-control').on('change', awsTags);
    awsTags();


    prodNotes = function () {
        newLines = "\n";
        newLines += "export HOSTLIST=(" + $('#productionNotesHostList').val() + ")\n";
        newLines += "export PREFIX=\"" + $('#productionNotesExternalDNSPrefix').val() + "\"\n";
        newLines += "export KEYPATH=\"" + $('#productionNotesKeyPath').val() + "\"";
        $("#productionNotes code").text(replaceFirstNLines($("#productionNotes code").text(), newLines));
    }
    $('#productionNotes .form-control').on('change', prodNotes);

    automationAgents = function () {
        newLines = "\n";
        newLines += "export HOSTLIST=(" + $('#automationAgentsHostList').val() + ")\n";
        newLines += "export PREFIX=\"" + $('#automationAgentsExternalDNSPrefix').val() + "\"\n";
        newLines += "export KEYPATH=\"" + $('#automationAgentsKeyPath').val() + "\"\n";
        newLines += "export GROUPID=\"" + $('#automationAgentsGroupId').val() + "\"\n";
        newLines += "export AGENTSAPIKEY=\"" + $('#automationAgentsApiKey').val() + "\"";
        $("#automationAgents code").text(replaceFirstNLines($("#automationAgents code").text(), newLines));
    }
    $('#automationAgents .form-control').on('change', automationAgents);

    configMongod = function () {
        newLines = "\n";
        newLines += "export PATH=\"" + $('#configMongodPath').val() + "\"\n";
        newLines += "export DBPATH=\"$DATAPATH" + $('#configMongodDBPath').val() + "\"\n";
        newLines += "export LOGPATH=\"$DATAPATH" + $('#configMongodLogPath').val() + "\"\n";
        newLines += "export KEYPATH=\"$DATAPATH" + $('#configMongodKeyPath').val() + "\"\n";
        newLines += "export KEYFILE=\"" + $('#configMongodKey').val() + "\"\n";
        $("#configMongod code").text(replaceFirstNLines($("#configMongod code").text(), newLines));
    }
    $('#configMongod .form-control').on('change', configMongod);


    configRS = function () {
        newLines = "\n";
        newLines += "export RSNAME=\"" + $('#configRSName').val() + "\"\n";
        $("#configRS code").text(replaceFirstNLines($("#configRS code").text(), newLines));
    }
    $('#configRS .form-control').on('change', configRS);

    replicaSet = function () {
        newLines = "\n";
        newLines += "mongo --eval 'rs.initiate(" + $('#replicaSetDocument').val() + ")'";
        $("#replicaSet code").text(newLines);
    }
    $('#replicaSet .form-control').on('change', replicaSet);

    opsManager = function () {
        newLines = "\n";
        newLines += "export VERSION=\"" + $('#opsManagerVersion').val() + "\"";
        $("#opsManager code").text(newLines);
    }
    $('#opsManager .form-control').on('change', replicaSet);

    generateTLSCert = function () {
        newLines = "\n";
        newLines += "export HOSTLIST=(" + $('#generateTLSCertHostList').val() + ")\n";
        newLines += "export ALTNAME=\"" + $('#generateTLSCertAltName').val() + "\"\n";
        newLines += "export C=\"" + $('#generateTLSCertC').val() + "\"\n";
        newLines += "export ST=\"" + $('#generateTLSCertST').val() + "\"\n";
        newLines += "export L=\"" + $('#generateTLSCertL').val() + "\"\n";
        newLines += "export O=\"" + $('#generateTLSCertO').val() + "\"\n";
        newLines += "export OU=\"" + $('#generateTLSCertOU').val() + "\"\n";
        newLines += "export ROOTCN=\"" + $('#generateTLSCertCN').val() + "\"\n";
        $("#generateTLSCert code").text(replaceFirstNLines($("#generateTLSCert code").text(), newLines));
    }
    $('#generateTLSCert .form-control').on('change', generateTLSCert);
});
function replaceFirstNLines(originalText, newLines) {
    let lines = originalText.split('\n');
    lines.splice(0, newLines.split("\n").length);
    const newArray = [newLines].concat(lines)
    return newArray.join('\n');
}
