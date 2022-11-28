document.addEventListener('DOMContentLoaded', () => {

    document.querySelector("#signOutLink").addEventListener('click', () => {
        document.querySelector("#signOutForm").submit();
    });

    Array.from(document.getElementsByClassName("userStatusChangeLink"))
        .forEach(link => link.addEventListener('click', () => {
            document.getElementById('changeUserId')
                .setAttribute('value', link.getAttribute('data-id'))
            document.getElementById('changeUserStatus')
                .setAttribute('value', link.getAttribute('data-status'))
            document.getElementById('changeUsername')
                .innerText = link.getAttribute('data-user');
            document.getElementById('changeUserStatusBefore')
                .innerText = link.getAttribute('data-status') == true ? 'Active' : 'Suspend';
            document.getElementById('changeUserStatusAfter')
                .innerText = link.getAttribute('data-status') == true ? 'Suspend' : 'Active';

            const dialog = new bootstrap.Modal('#userStatusChangeModal');
            dialog.show();
        }))
})