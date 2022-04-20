
$(window).on('load', function() {
    removeServers = function(){
        newLines = "\n";
        newLines += "export REGIONS=("+$('#removeServersRegions').val()+")\n";
        newLines += "export OWNER=\""+$('#removeServersOwner').val()+"\"\n";
        newLines += "export TAGNAME=\""+$('#removeServersTagName').val()+"\"";
        $("#removeServers code").text(replaceFirstNLines($("#removeServers code").text(), newLines));
    }
    $('#removeServers .form-control').on('change', removeServers);

    launchInstances = function(){
        newLines = "\n";
        newLines += "export REGION=\""+$('#launchInstanceRegion').val()+"\"\n";
        newLines += "export SG_NAME=\""+$('#launchInstanceSGName').val()+"\"\n";
        newLines += "export SG_DESCRIPTION=\""+$('#launchInstanceSGDesc').val()+"\"\n";
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

});
function replaceFirstNLines(originalText, newLines){
    let lines = originalText.split('\n');
    lines.splice(0,newLines.split("\n").length);
    const newArray = [newLines].concat(lines)
    return newArray.join('\n');
}
