
$(window).on('load', function() {
    removeServers = function(){
        newLines = "\n";
        newLines += "export REGIONS=("+$('#removeServersRegions').val()+")\n";
        newLines += "export OWNER=\""+$('#removeServersOwner').val()+"\"\n";
        newLines += "export TAGNAME=\""+$('#removeServersTagName').val()+"\"";
        $("#removeServers code").text(replaceFirstNLines($("#removeServers code").text(), newLines));
    }
    $('#removeServers .form-control').on('change', removeServers);

    securityGroup = function(){
        newLines = "\n";
        newLines += "export REGION=\""+$('#securityGroupRegion').val()+"\"\n";
        newLines += "export SG_NAME=\""+$('#securityGroupName').val()+"\"\n";
        newLines += "export SG_DESCRIPTION=\""+$('#securityGroupDesc').val()+"\"\n";
        $("#securityGroup code").text(replaceFirstNLines($("#securityGroup code").text(), newLines));
    }
    $('#securityGroup .form-control').on('change', securityGroup);
    
    
    launchInstances = function(){
        newLines = "\n";
        newLines += "export REGION=\""+$('#launchInstanceRegion').val()+"\"\n";
        newLines += "export SG_NAME=\""+$('#launchInstanceSGName').val()+"\"\n";
        newLines += "export PG_NAME=\""+$('#launchInstancePGName').val()+"\"\n";
        newLines += "export ITYPE=\""+$('#launchInstanceInstanceType').val()+"\"\n";
        newLines += "export IMG_NAME=\""+$('#launchInstanceImageName').val()+"\"\n";
        newLines += "export DISKS='"+$('#launchInstanceDisks').val()+"'\n";
        newLines += "export KEY_NAME=\""+$('#launchInstanceKeyName').val()+"\"\n";
        newLines += "export TAGS='"+$('#launchInstanceTags').val()+"'\n";
        newLines += "export NO_OF_HOST="+$('#launchInstanceNoOfHosts').val();
        $("#launchInstance code").text(replaceFirstNLines($("#launchInstance code").text(), newLines));
    }
    $('#launchInstance .form-control').on('change', launchInstances);
    
    dns = function(){
        newLines = "\n";
        newLines += "export DOMAIN=\""+$('#dnsDomain').val()+"\"\n";
        newLines += "export HOSTEDZONE=\""+$('#dnsHostedZoneId').val()+"\"\n";
        newLines += "export INTERNAL_HOSTLIST="+$('#dnsInternalHostList').val()+"\n";
        newLines += "export INTERNAL_SUBDOMAIN=\""+$('#dnsInternalSubdomainPrefix').val()+"\"\n";
        newLines += "export EXTERNAL_HOSTLIST="+$('#dnsExternalHostList').val()+"\n";
        newLines += "export EXTERNAL_SUBDOMAIN=\""+$('#dnsExternalSubdomainPrefix').val()+"\"";
        $("#dns code").text(replaceFirstNLines($("#dns code").text(), newLines));
    }
    $('#dns .form-control').on('change', dns);

    prodNotes = function(){
        newLines = "\n";
        newLines += "export HOSTLIST=("+$('#productionNotesHostList').val()+")\n";
        newLines += "export PREFIX=\""+$('#productionNotesExternalDNSPrefix').val()+"\"\n";
        newLines += "export KEYPATH=\""+$('#productionNotesKeyPath').val()+"\"";
        $("#productionNotes code").text(replaceFirstNLines($("#productionNotes code").text(), newLines));
    }
    $('#productionNotes .form-control').on('change', prodNotes);

    automationAgents = function(){
        newLines = "\n";
        newLines += "export HOSTLIST=("+$('#automationAgentsHostList').val()+")\n";
        newLines += "export PREFIX=\""+$('#automationAgentsExternalDNSPrefix').val()+"\"\n";
        newLines += "export KEYPATH=\""+$('#automationAgentsKeyPath').val()+"\"\n";
        newLines += "export GROUPID=\""+$('#automationAgentsGroupId').val()+"\"\n";
        newLines += "export AGENTSAPIKEY=\""+$('#automationAgentsApiKey').val()+"\"";
        $("#automationAgents code").text(replaceFirstNLines($("#automationAgents code").text(), newLines));
    }
    $('#automationAgents .form-control').on('change', automationAgents);

    configMongod = function(){
        newLines = "\n";
        newLines += "export PATH=\""+$('#configMongodPath').val()+"\"\n";
        newLines += "export DBPATH=\"$PATH"+$('#configMongodDBPath').val()+"\"\n";
        newLines += "export LOGPATH=\"$PATH"+$('#configMongodLogPath').val()+"\"\n";
        newLines += "export KEYPATH=\"$PATH"+$('#configMongodKeyPath').val()+"\"\n";
        $("#configMongod code").text(replaceFirstNLines($("#configMongod code").text(), newLines));
    }
    $('#configMongod .form-control').on('change', configMongod);

    configRS = function(){
        newLines = "\n";
        newLines += "export RSNAME=\""+$('#configRSName').val()+"\"\n";
        $("#configRS code").text(replaceFirstNLines($("#configRS code").text(), newLines));
    }
    $('#configRS .form-control').on('change', configRS);

    replicaSet = function(){
        newLines = "\n";
        newLines += "mongo --eval 'rs.initiate("+$('#replicaSetDocument').val()+")'";
        $("#replicaSet code").text(newLines);
    }
    $('#replicaSet .form-control').on('change', replicaSet);

    generateTLSCert = function(){
        newLines = "\n";
        newLines += "export HOSTLIST=("+$('#generateTLSCertHostList').val()+")\n";
        newLines += "export ALTNAME=\""+$('#generateTLSCertAltName').val()+"\"\n";
        newLines += "export C=\""+$('#generateTLSCertC').val()+"\"\n";
        newLines += "export ST=\""+$('#generateTLSCertST').val()+"\"\n";
        newLines += "export L=\""+$('#generateTLSCertL').val()+"\"\n";
        newLines += "export O=\""+$('#generateTLSCertO').val()+"\"\n";
        newLines += "export OU=\""+$('#generateTLSCertOU').val()+"\"\n";
        newLines += "export ROOTCN=\""+$('#generateTLSCertCN').val()+"\"\n";
        $("#generateTLSCert code").text(replaceFirstNLines($("#generateTLSCert code").text(), newLines));
    }
    $('#generateTLSCert .form-control').on('change', generateTLSCert);
});
function replaceFirstNLines(originalText, newLines){
    let lines = originalText.split('\n');
    lines.splice(0,newLines.split("\n").length);
    const newArray = [newLines].concat(lines)
    return newArray.join('\n');
}
